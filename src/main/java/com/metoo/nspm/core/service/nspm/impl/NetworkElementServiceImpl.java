package com.metoo.nspm.core.service.nspm.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.metoo.nspm.core.manager.admin.tools.DateTools;
import com.metoo.nspm.core.manager.admin.tools.GroupTools;
import com.metoo.nspm.core.manager.admin.tools.ShiroUserHolder;
import com.metoo.nspm.core.mapper.nspm.NetworkElementMapper;
import com.metoo.nspm.core.service.nspm.*;
import com.metoo.nspm.core.utils.ResponseUtil;
import com.metoo.nspm.dto.NetworkElementDto;
import com.metoo.nspm.entity.nspm.*;
import com.metoo.nspm.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@Service
@Transactional
public class NetworkElementServiceImpl implements INetworkElementService {

    @Autowired
    private NetworkElementMapper networkElementMapper;
    @Autowired
    private IGroupService groupService;
    @Autowired
    private GroupTools groupTools;
    @Autowired
    private IConfigBackupService configBackupService;
    @Autowired
    private IVendorService vendorService;
    @Autowired
    private ICredentialService credentialService;
    @Autowired
    private IAccessoryService accessoryService;
    @Autowired
    private INetworkElementAccessoryService networkElementAccessoryService;

    @Override
    public NetworkElement selectObjById(Long id) {
        return this.networkElementMapper.selectObjById(id);
    }

    @Override
    public NetworkElement selectObjByUuid(String uuid) {
        return this.networkElementMapper.selectObjByUuid(uuid);
    }


    @Override
    public Page<NetworkElement> selectConditionQuery(NetworkElementDto instance) {
        if(instance == null){
            instance = new NetworkElementDto();
        }
        Page<NetworkElement> page = PageHelper.startPage(instance.getCurrentPage(), instance.getPageSize());
        this.networkElementMapper.selectConditionQuery(instance);
        return page;
}

    @Override
    public List<NetworkElement> selectObjByMap(Map params) {

        return this.networkElementMapper.selectObjByMap(params);
    }

    @Override
    public List<NetworkElement> selectObjAll() {
        User user = ShiroUserHolder.currentUser();
        if(user.getGroupId() != null){
            Group group = this.groupService.selectObjById(user.getGroupId());
            Map params = new HashMap();
            List<Long> groupList = new ArrayList<>();
            if(group != null){
                Set<Long> ids = this.groupTools.genericGroupId(group.getId());
                params.put("groupIds", ids);
                return this.networkElementMapper.selectObjAll(params);
            }
        }
        return new ArrayList<>();
    }

    @Override
    public int save(NetworkElement instance) {
        if(instance.getId() == null){
            instance.setAddTime(new Date());
            instance.setUuid(UUID.randomUUID().toString());
            User user = ShiroUserHolder.currentUser();
            instance.setUserId(user.getId());
            instance.setUserName(user.getUsername());
            try {
                return this.networkElementMapper.save(instance);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }else{
            try {
                return this.networkElementMapper.update(instance);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

    @Override
    public int batchInsert(List<NetworkElement> instances) {
        for (NetworkElement instance : instances) {
            instance.setAddTime(new Date());
            instance.setUuid(UUID.randomUUID().toString());
            User user = ShiroUserHolder.currentUser();
            instance.setUserId(user.getId());
            instance.setUserName(user.getUsername());
            Group group = this.groupService.selectObjById(user.getGroupId());
            if(group != null){
                instance.setGroupId(group.getId());
                instance.setGroupName(group.getBranchName());
            }
        }
        try {
            int i = this.networkElementMapper.batchInsert(instances);
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(NetworkElement instance) {
        try {
            this.networkElementMapper.update(instance);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int del(Long id) {
        try {
            this.networkElementMapper.del(id);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Object backup(NetworkElement instance) {
        if(instance != null){
            if(!instance.isPermitConnect()){
                return ResponseUtil.badArgument("未配置登录凭据");
            }
            ConfigBackup configBackup = this.configBackupService.getInstance();
            if(configBackup != null){
                Date currentTime = new Date();
                String time = DateTools.getCurrentDate(currentTime);
                configBackup.setHost(instance.getIp());
                configBackup.setHostName(instance.getDeviceName());
                configBackup.setBackupTime(time);
                configBackup.setUuid(instance.getUuid());
                if(instance.isPermitConnect()){
                    configBackup.setCommType(instance.getConnectType() == 0 ? "SSH" : "WEB");
                    configBackup.setPort(instance.getPort());
                }
                if(instance.getVendorId() != null){
                    Vendor vendor = this.vendorService.selectObjById(instance.getVendorId());
                    if(vendor != null){
                        configBackup.setVendor(vendor.getNameEn());
                    }
                }
                Credential credential = this.credentialService.getObjById(instance.getCredentialId());
                if(credential != null){
                    configBackup.setLoginName(credential.getLoginName());
                    configBackup.setLoginPassword(credential.getLoginPassword());
                    configBackup.setEnablePassword(credential.getEnablePassword());
                }

                String[] args1 = new String[] {
                        "python", configBackup.getScript(),
                        JSON.toJSONString(configBackup)};
                try {
                    //第二个为python脚本所在位置，后面的为所传参数（得是字符串类型）
                    Process proc = Runtime.getRuntime().exec(args1);// 执行py文件
                    BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"gb2312"));//解决中文乱码，参数可传中文
                    String line = null;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                    in.close();
                    int exitVal = proc.waitFor();
                    log.info("=======================Process exitValue: " + exitVal);
                    if(exitVal == 0){
                        Accessory accessory = new Accessory();
                        accessory.setAddTime(currentTime);
                        String fileName = instance.getDeviceName()
                                            + "_"
                                            + instance.getIp()
                                            + "_"
                                            + instance.getUuid()
                                            + "_"
                                            + time;
                        accessory.setA_name(fileName);
                        accessory.setA_path(configBackup.getFileHome());
                        accessory.setA_ext(".conf");
                        accessory.setFrom(1);
                        this.accessoryService.save(accessory);
                        NeAccessory na = new NeAccessory();
                        na.setNeId(instance.getId());
                        na.setAccessoryId(accessory.getId());
                        this.networkElementAccessoryService.save(na);
                        return ResponseUtil.ok();
                    }
                    return ResponseUtil.badArgument("备份失败");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return ResponseUtil.badArgument("配置信息错误");
    }

    @Override
    public NetworkElement selectAccessoryByUuid(String uuid) {

        return this.networkElementMapper.selectAccessoryByUuid(uuid);
    }

    @Override
    public Object delConfig(Long id) {
        NeAccessory neAccessory = this.networkElementAccessoryService.selectObjById(id);
        if(neAccessory != null){
            NetworkElement networkElement = this.selectObjById(neAccessory.getNeId());
            if(networkElement != null){
                Accessory accessory = this.accessoryService.getObjById(neAccessory.getAccessoryId());
                if(accessory != null){
                    // 读取文件
                    try {
                        this.accessoryService.delete(accessory.getId());
                        try {
                            this.networkElementAccessoryService.delete(neAccessory.getId());
                        } catch (Exception e) {
                            e.printStackTrace();
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                            return ResponseUtil.error("删除失败");
                        }
                        String path = accessory.getA_path() + accessory.getA_name() + accessory.getA_ext();
                        File file = new File(path);
                        if(file.exists()){
                            try {
                                file.delete();
                                return ResponseUtil.ok();
                            } catch (Exception e) {
                                e.printStackTrace();
                                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                                return ResponseUtil.error("删除失败");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return ResponseUtil.error("删除失败");
                    }
                    return ResponseUtil.ok();
                }
            }
        }
        return ResponseUtil.badArgument();
    }
}
