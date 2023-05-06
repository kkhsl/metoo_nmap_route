package com.metoo.nspm.core.manager.admin.action;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.metoo.nspm.core.manager.admin.tools.GroupTools;
import com.metoo.nspm.core.manager.admin.tools.ShiroUserHolder;
import com.metoo.nspm.core.mapper.zabbix.ItemMapper;
import com.metoo.nspm.core.service.nspm.IDomainService;
import com.metoo.nspm.core.service.nspm.IGroupService;
import com.metoo.nspm.core.service.nspm.IVlanService;
import com.metoo.nspm.core.service.nspm.ISubnetService;
import com.metoo.nspm.core.service.topo.ITopoNodeService;
import com.metoo.nspm.core.service.zabbix.ItemService;
import com.metoo.nspm.core.service.zabbix.impl.IListUtils;
import com.metoo.nspm.core.utils.MyStringUtils;
import com.metoo.nspm.core.utils.ResponseUtil;
import com.metoo.nspm.core.utils.collections.ListSortUtil;
import com.metoo.nspm.core.utils.network.IpUtil;
import com.metoo.nspm.entity.nspm.*;
import com.metoo.nspm.entity.zabbix.Item;
import com.metoo.nspm.entity.zabbix.ItemTag;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description Vlan管理
 *
 * @author HKK
 *
 * @create 2023/02/22
 *
 */
@RequestMapping("/admin/vlan")
@RestController
public class VlanManagerController {

    @Autowired
    private IVlanService vlanService;
    @Autowired
    private IDomainService domainService;
    @Autowired
    private IGroupService groupService;
    @Autowired
    private ISubnetService subnetService;
    @Autowired
    private GroupTools groupTools;
    @Autowired
    private ITopoNodeService topoNodeService;
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemService itemService;

    @ApiOperation("Vlan列表")
    @GetMapping("/asd")
    public Object lasdfist(){
        Map params = new HashMap();
        params.put("orderBy", "addTime");
        params.put("orderType", "desc");
        List<Vlan> vlans = this.vlanService.selectObjByMap(params);
        return ResponseUtil.ok(vlans);
    }

    @ApiOperation("Vlan列表")
    @GetMapping
    public Object list(@RequestParam(value = "domainId",required = false) Long domainId){
            User user = ShiroUserHolder.currentUser();
            Group group = this.groupService.selectObjById(user.getGroupId());
            if(group != null){
                Map params = new HashMap();
                Set<Long> ids = this.groupTools.genericGroupId(group.getId());
                params.put("groupIds", ids);
                Domain domain = this.domainService.selectObjById(domainId);
                if(domain != null){
                    params.put("domainId", domain.getId());
                }
                params.put("hidden", false);
                params.put("orderBy", "number");
                params.put("orderType", "DESC");
                List<Vlan> vlans = this.vlanService.selectObjByMap(params);
                vlans.stream().forEach(e -> {
                    if(e.getSubnetId() != null && !e.getSubnetId().equals("")) {
                        Subnet subnet = this.subnetService.selectObjById(e.getSubnetId());
                        e.setSubnetIp(subnet.getIp());
                        e.setMaskBit(subnet.getMask());
                    }
                });
                return ResponseUtil.ok(vlans);

        }
        return ResponseUtil.ok();
    }

    @ApiOperation("Vlan添加")
    @GetMapping("/add")
    public Object add(){
        Map map = new HashMap();
        Map params = new HashMap();
        List<Domain> domains = this.domainService.selectObjByMap(params);
        map.put("domain", domains);
        List<Subnet> parentList = this.subnetService.selectSubnetByParentId(null);
        if(parentList.size() > 0){
            for (Subnet subnet : parentList) {
                this.genericSubnet(subnet);
            }
        }
        map.put("subnet", parentList);
        return ResponseUtil.ok(map);
    }

    public List<Subnet> genericSubnet(Subnet subnet){
        List<Subnet> subnets = this.subnetService.selectSubnetByParentId(subnet.getId());
        if(subnets.size() > 0){
            for(Subnet child : subnets){
                List<Subnet> subnetList = genericSubnet(child);
                if(subnetList.size() > 0){
                    child.setSubnetList(subnetList);
                }
            }
            subnet.setSubnetList(subnets);
        }
        return subnets;
    }

    @ApiOperation("Vlan更新,数据回显")
    @GetMapping("/update")
    public Object updadte(@RequestParam(value = "id") Long id){
        Map map = new HashMap();
        Vlan vlan = this.vlanService.selectObjById(id);
        if(vlan == null){
            return ResponseUtil.badArgument("Vlan不存在");
        }
        Domain domain = this.domainService.selectObjById(vlan.getDomainId());
        if(domain != null){
            vlan.setDomainName(domain.getName());
        }
        if(vlan.getSubnetId() != null){
            Subnet subnet = this.subnetService.selectObjById(vlan.getSubnetId());
            vlan.setSubnetIp(subnet.getIp());
            vlan.setMaskBit(subnet.getMask());
        }
        map.put("vlan", vlan);
        Map params = new HashMap();
        List<Domain> domains = this.domainService.selectObjByMap(params);
        map.put("domain", domains);
        List<Subnet> parentList = this.subnetService.selectSubnetByParentId(null);
        if(parentList.size() > 0){
            for (Subnet subnet : parentList) {
                this.genericSubnet(subnet);
            }
        }
        map.put("subnet", parentList);
        return ResponseUtil.ok(map);
    }

    @ApiOperation("创建/修改")
    @PostMapping
    public Object save(@RequestBody Vlan vlan){
//        if(vlan.getName() == null || vlan.getName().equals("")){
//            return ResponseUtil.badArgument("名称不能为空");
//        }else{
//            Map params = new HashMap();
//            params.put("vlanId", vlan.getId());
//            params.put("name", vlan.getName());
//            // 当前分组内不重名
//            User user = ShiroUserHolder.currentUser();
//            Group group = this.groupService.selectObjById(user.getGroupId());
//            if(group != null) {
//                Set<Long> ids = this.groupTools.genericGroupId(group.getId());
//                params.put("groupIds", ids);
//            }
//            List<Vlan> domains = this.vlanService.selectObjByMap(params);
//            if(domains.size() > 0){
//                return ResponseUtil.badArgument("名称重复");
//            }
//        }
        if(vlan.getDomainId() != null && !vlan.getDomainId().equals("")){
            Domain domain = this.domainService.selectObjById(vlan.getDomainId());
            if(domain == null){
                return ResponseUtil.badArgument("二层域不存在");
            }
        }
        if(vlan.getSubnetId() != null){
            Subnet subnet = this.subnetService.selectObjById(vlan.getSubnetId());
            if(subnet == null){
                return ResponseUtil.badArgument("网段不存在");
            }
        }
        if(vlan.getNumber() != 0){
            return ResponseUtil.badArgument("Vlan号不允许编辑");
        }
        int result = this.vlanService.save(vlan);
        if(result >= 1){
            return ResponseUtil.ok();
        }
        return ResponseUtil.error();
    }

    @ApiOperation("删除")
    @DeleteMapping
    public Object delete(String ids){
        for (String id : ids.split(",")){
            Vlan vlan = this.vlanService.selectObjById(Long.parseLong(id));
            if(vlan == null){
                return ResponseUtil.badArgument();
            }
            int i = this.vlanService.delete(Long.parseLong(id));
            if(i <= 0){
                return ResponseUtil.error();
            }
        }
        return ResponseUtil.ok();
    }

    @ApiOperation("采集Zabbix vlan")
    @GetMapping("/comb")
    public Object gatherVlan(){
        // 暂时放外面
        int POOL_SIZE = Integer.max(Runtime.getRuntime().availableProcessors(), 0);
        ExecutorService es = Executors.newFixedThreadPool(POOL_SIZE);
        List<Map> devices = this.topoNodeService.queryNetworkElement();
        if (devices != null && devices.size() > 0) {
            Map params = new HashMap();
            List<Integer> list = new Vector();
            final CountDownLatch latch = new CountDownLatch(devices.size());
            for (Map device : devices) {
                es.execute(() ->{
                    params.clear();
                    params.put("ip", device.get("ip"));
                    params.put("obj", "totalvlan");
                    List<Item> itemTags = this.itemMapper.selectItemTagByIpAndObj(params);
                    if(itemTags.size() > 0) {
                            itemTags.stream().forEach(item -> {
                                List<ItemTag> tags = item.getItemTags();
                                if (tags != null && tags.size() > 0) {
                                    for (ItemTag tag : tags) {
                                        String value = tag.getValue();
                                        if(tag.getTag().equals("vlan")){
                                            int i = MyStringUtils.acquireCharacterPosition(value, " ", 1);
                                            if(i > 0){
                                                value = value.substring(i).trim();
                                                list.add(Integer.parseInt(value));
                                            }
                                        }
                                    }
                                }
                            });

                        }
                        latch.countDown();
                    });
                }
            try {
                latch.await();// 等待结果线程池线程执行结束
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(list != null && list.size() > 0){

                Map<Integer, String> vlanMap = this.itemService.gatherIpaddressItem();

                Domain domain = null;
                params.clear();
                params.put("name", "默认二层域");
                List<Domain> domains = this.domainService.selectObjByMap(params);
                if(domains.size() > 0){
                    domain = domains.get(0);
                }
                HashSet<Integer> set = new HashSet<>(list);
                for (Integer integer : set) {
                    Vlan vlan = new Vlan();
                    vlan.setNumber(integer);
                    if(domain != null){
                        vlan.setDomainId(domain.getId());
                    }
                    params.clear();
                    params.put("number", integer);

                    String value = vlanMap.get(integer);
                    if(Strings.isNotBlank(value) && value.contains("&")){
                        String[] values = value.split("&");
                        Map args = new HashMap();
                        args.put("ip", IpUtil.ipConvertDec(values[0]));
                        args.put("mask", values[1]);
                        List<Subnet> subnets = this.subnetService.selectObjByMap(args);
                        if(subnets.size() > 0){
                            vlan.setSubnetId(subnets.get(0).getId());
                            params.put("subnetId", subnets.get(0).getId());
                        }
                    }
                    List<Vlan> vlans = this.vlanService.selectObjByMap(params);
                    if(vlans.size() > 0){
                        Vlan vlan1 = vlans.get(0);
                        vlan1.setNumber(integer);
                        vlan1.setHidden(false);
                        if(domain != null){
                            vlan1.setDomainId(domain.getId());
                        }
                        this.vlanService.update(vlan1);
                    }else{
                        vlan.setHidden(false);
                        this.vlanService.save(vlan);
                    }
                }
                // 隐藏
                params.clear();
                params.put("notNumbers", set);
                List<Vlan> vlans = this.vlanService.selectObjByMap(params);
                if(vlans.size() > 0){
                    vlans.forEach(e -> {
                        e.setHidden(true);
                        this.vlanService.update(e);
                    });
                }
                return ResponseUtil.ok(set);
            }
            return ResponseUtil.ok();
        }
        return ResponseUtil.ok();
    }
}
