package com.metoo.nspm.core.manager.admin.python;

import com.alibaba.fastjson.JSON;
import com.metoo.nspm.core.manager.admin.tools.DateTools;
import com.metoo.nspm.core.service.nspm.IConfigBackupService;
import com.metoo.nspm.core.service.nspm.ICredentialService;
import com.metoo.nspm.core.service.nspm.INetworkElementService;
import com.metoo.nspm.core.service.nspm.IVendorService;
import com.metoo.nspm.core.utils.ResponseUtil;
import com.metoo.nspm.entity.nspm.ConfigBackup;
import com.metoo.nspm.entity.nspm.Credential;
import com.metoo.nspm.entity.nspm.NetworkElement;
import com.metoo.nspm.entity.nspm.Vendor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class TestRunPythonScript {

    public static void main(String[] args) {

        try {
            //第二个为python脚本所在位置，后面的为所传参数（得是字符串类型）
            String[] args1 = new String[] {
                    "python", "/opt/nmap/service/py/ruijie/ruijie-rgos-metoo.py",
                    "192.168.5.191",
                    "metoo@domain",
                    "Metoo@89745000",
                    "abc1234567"};
            Process proc = Runtime.getRuntime().exec(args1);// 执行py文件

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"gb2312"));//解决中文乱码，参数可传中文
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/admin/exec_py")
    public void execPy(){
        try {
            //第二个为python脚本所在位置，后面的为所传参数（得是字符串类型）
            String[] args1 = new String[] {
                    "python", "/opt/nmap/service/py/ruijie/ruijie-rgos-metoo.py",
                    "192.168.5.191",
                    "metoo@domain",
                    "Metoo@89745000",
                    "abc1234567"};
            Process proc = Runtime.getRuntime().exec(args1);// 执行py文件


//            Process proc2 = Runtime.getRuntime().exec("\\n");// 执行回车
//            proc2.waitFor();

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"gb2312"));//解决中文乱码，参数可传中文
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test(){

        Map params = new HashMap();
        params.put("host", "192.168.5.191");
        params.put("loginName", "metoo@domain");
        params.put("loginPassword", "Metoo@89745000");
        params.put("enablePassword", "ruijie");
        params.put("hostName", "ruijie");
        params.put("uuid", "123456");
        params.put("fileHome", "/opt/nmap/service/py");
        params.put("backupTime", "202300112233");
        params.put("commType", "SSH");
        params.put("vendor", "ruijie");
        System.out.println(JSON.toJSONString(params));
    }

    @GetMapping("/admin/exec_py/json")
    public void execPyJson(){
        try {
            //第二个为python脚本所在位置，后面的为所传参数（得是字符串类型）
            Map params = new HashMap();
            params.put("host", "192.168.5.191");
            params.put("loginName", "metoo@domain");
            params.put("loginPassword", "Metoo@89745000");
            params.put("enablePassword", "ruijie");
            params.put("hostName", "ruijie");
            params.put("uuid", "123456");
            params.put("fileHome", "/opt/nmap/service/py/configFileInbox/");
            params.put("backupTime", "202300112233");
            params.put("commType", "SSH");
            params.put("vendor", "ruijie");
            params.put("port", 22);

//            String[] args1 = new String[] {
////                    "python", "/opt/nmap/service/py/ruijie/ruijie-rgos-metoo.py",
////                    "192.168.5.191",
////                    "metoo@domain",
////                    "Metoo@89745000",
////                    "abc1234567"};

              String[] args1 = new String[] {
                    "python", "/opt/nmap/service/py/getconfig.py",
                      JSON.toJSONString(params)};

            Process proc = Runtime.getRuntime().exec(args1);// 执行py文件

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(),"gb2312"));//解决中文乱码，参数可传中文
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            int exitVal = proc.waitFor();
            log.info("=======================Process exitValue: " + exitVal);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Autowired
    private INetworkElementService networkElementService;
    @Autowired
    private IConfigBackupService configBackupService;
    @Autowired
    private IVendorService vendorService;
    @Autowired
    private ICredentialService credentialService;

    @GetMapping("/admin/exec_py/json2")
    public void execPyJso2n(){
            //第二个为python脚本所在位置，后面的为所传参数（得是字符串类型）
            NetworkElement instance = this.networkElementService.selectObjByUuid("896dcf46-2f19-4ba3-8976-cfe859f6231d");
            if (instance != null) {
                ConfigBackup configBackup = this.configBackupService.getInstance();
                if (configBackup != null) {
                    configBackup.setHost(instance.getIp());
                    configBackup.setHostName(instance.getDeviceName());
                    configBackup.setBackupTime(DateTools.getCurrentDate(new Date()));
                    configBackup.setUuid(instance.getUuid());
                    if (instance.isPermitConnect()) {
                        configBackup.setCommType(instance.getConnectType() == 0 ? "SSH" : "WEB");
                        configBackup.setPort(instance.getPort());
                    }
                    if (instance.getVendorId() != null) {
                        Vendor vendor = this.vendorService.selectObjById(instance.getVendorId());
                        if (vendor != null) {
                            configBackup.setVendor(vendor.getNameEn());
                        }
                    }
                    Credential credential = this.credentialService.getObjById(instance.getCredentialId());
                    if (credential != null) {
                        configBackup.setLoginName(credential.getLoginName());
                        configBackup.setLoginPassword(credential.getLoginPassword());
                        configBackup.setEnablePassword(credential.getEnablePassword());
                    }

                    String[] args1 = new String[]{
                            "python", "/opt/nmap/service/py/getconfig.py",
                            JSON.toJSONString(configBackup)};
                    try {
                        //第二个为python脚本所在位置，后面的为所传参数（得是字符串类型）
                        Process proc = Runtime.getRuntime().exec(args1);// 执行py文件
                        BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream(), "gb2312"));//解决中文乱码，参数可传中文
                        String line = null;
                        while ((line = in.readLine()) != null) {
                            System.out.println(line);
                        }
                        in.close();
                        int exitVal = proc.waitFor();
                        log.info("=======================Process exitValue: " + exitVal);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
    }

}
