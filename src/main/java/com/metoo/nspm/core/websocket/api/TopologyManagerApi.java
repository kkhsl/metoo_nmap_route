package com.metoo.nspm.core.websocket.api;

import com.alibaba.fastjson.JSONObject;
import com.metoo.nspm.core.config.websocket.demo.NoticeWebsocketResp;
import com.metoo.nspm.core.manager.admin.tools.DateTools;
import com.metoo.nspm.core.manager.admin.tools.MacUtil;
import com.metoo.nspm.core.service.api.zabbix.ZabbixService;
import com.metoo.nspm.core.service.nspm.*;
import com.metoo.nspm.core.service.zabbix.ItemService;
import com.metoo.nspm.core.utils.network.IpUtil;
import com.metoo.nspm.entity.nspm.*;
import com.metoo.nspm.entity.zabbix.Item;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/websocket/api/zabbix")
@RestController
public class TopologyManagerApi {

    @Autowired
    private IMacHistoryService macHistoryService;
    @Autowired
    private INetworkElementService networkElementService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ZabbixService zabbixService;
    @Autowired
    private MacUtil macUtil;
    @Autowired
    private ISubnetService subnetService;
    @Autowired
    private IVlanService vlanService;
    @Autowired
    private ITerminalService terminalService;
    @Autowired
    private IDeviceTypeService deviceTypeService;
    @Autowired
    private RedisResponseUtils redisResponseUtils;

    @ApiOperation("设备 Mac (DT))")
    @GetMapping(value = {"/mac/dt"})
    public NoticeWebsocketResp getObjMac(@RequestParam(value = "requestParams", required = false) String requestParams) throws Exception {
        NoticeWebsocketResp rep = new NoticeWebsocketResp();
        if(!String.valueOf(requestParams).equals("")){
            Map params = JSONObject.parseObject(String.valueOf(requestParams), Map.class);
            String sessionId = (String)  params.get("sessionId");
            Date time = DateTools.parseDate(String.valueOf(params.get("time")), "yyyy-MM-dd HH:mm");
            List<String> list = JSONObject.parseObject(String.valueOf(params.get("params")), List.class);
            Map result = new HashMap();
            Map args = new HashMap();
            if(time == null){
                for (String uuid : list) {
                    args.clear();
                    args.put("uuid", uuid);
                    args.put("online", 1);
                    args.put("interfaceStatus", 1);
                    args.put("tag", "DT");
                    List<Terminal> terminals = this.terminalService.selectObjByMap(args);
                    terminals.stream().forEach(item -> {
                        String terminalIp = item.getIp();
                        if(StringUtils.isNotEmpty(terminalIp) && StringUtils.isEmpty(item.getVlan())){
                            // 获取网络地址
                            String network = IpUtil.getNBIP(terminalIp,"255.255.255.255", 0);
                            Subnet subnet = this.subnetService.selectObjByIp(network);
                            if(subnet != null){
                                if(subnet.getVlanId() != null && !subnet.getVlanId().equals("")){
                                    Vlan vlan = this.vlanService.selectObjById(subnet.getVlanId());
                                    if(vlan != null){
                                        item.setVlan(vlan.getName());
                                    }
                                }
                            }
                        }
                    });
                    this.macUtil.terminalJoint(terminals);
                    terminals.stream().forEach(e -> {
                        if(e.getDeviceTypeId() != null
                                && !e.getDeviceTypeId().equals("")){
//                            TerminalType terminalType = this.terminalTypeService.selectObjById(e.getTerminalTypeId());
//                            e.setTerminalTypeName(terminalType.getName());

                            DeviceType deviceType = this.deviceTypeService.selectObjById(e.getDeviceTypeId());
                            if(deviceType != null){
                                e.setDeviceTypeName(deviceType.getName());
                            }
                        }
                    });
                    result.put(uuid, terminals);
                }
            }else{
                for (String item : list) {
                    args.clear();
                    args.put("uuid", item);
                    args.put("tag", "DT");
                    args.put("time", time);
                    args.put("online", true);
                    args.put("interfaceStatus", 1);
                    List<Mac> macs = this.macHistoryService.selectByMap(args);
                    this.macUtil.macJoint(macs);
//                    this.macUtil.writerHistoryOnline(macs, time);
                    result.put(item, macs);
                }
            }
            rep.setNoticeType("4");
            rep.setNoticeStatus(1);
            rep.setNoticeInfo(result);
            this.redisResponseUtils.syncStrRedis(sessionId, JSONObject.toJSONString(result), 4);
            return rep;
        }
        rep.setNoticeType("4");
        rep.setNoticeStatus(0);
        return rep;
    }

    @ApiOperation("设备 Mac (DT))")
    @GetMapping(value = {"/mac/dt1"})
    public NoticeWebsocketResp getObjMac1(@RequestParam(value = "requestParams", required = false) String requestParams) throws Exception {
        NoticeWebsocketResp rep = new NoticeWebsocketResp();
        if(!String.valueOf(requestParams).equals("")){
            Map params = JSONObject.parseObject(String.valueOf(requestParams), Map.class);
            String sessionId = (String)  params.get("sessionId");
            Date time = DateTools.parseDate(String.valueOf(params.get("time")), "yyyy-MM-dd HH:mm");
            List<String> list = JSONObject.parseObject(String.valueOf(params.get("params")), List.class);
            Map result = new HashMap();
            Map args = new HashMap();
            if(time == null){
                for (String uuid : list) {
                    Map flux_terminal = new HashMap();
                    args.clear();
                    args.put("uuid", uuid);
                    args.put("online", 1);
                    args.put("interfaceStatus", 1);
                    args.put("tag", "DT");
//                List<Mac> macs = this.macService.selectByMap(args);
                    List<Terminal> terminals = this.terminalService.selectObjByMap(args);
                    terminals.stream().forEach(item -> {
                        String terminalIp = item.getIp();
                        if(StringUtils.isNotEmpty(terminalIp) && StringUtils.isEmpty(item.getVlan())){
                            // 获取网络地址
                            String network = IpUtil.getNBIP(terminalIp,"255.255.255.255", 0);
                            Subnet subnet = this.subnetService.selectObjByIp(network);
                            if(subnet != null){
                                if(subnet.getVlanId() != null && !subnet.getVlanId().equals("")){
                                    Vlan vlan = this.vlanService.selectObjById(subnet.getVlanId());
                                    if(vlan != null){
                                        item.setVlan(vlan.getName());
                                    }
                                }
                            }
                        }
                    });
                    this.macUtil.terminalJoint(terminals);
                    terminals.stream().forEach(e -> {
                        if(e.getDeviceTypeId() != null
                                && !e.getDeviceTypeId().equals("")){
//                            TerminalType terminalType = this.terminalTypeService.selectObjById(e.getTerminalTypeId());
//                            e.setTerminalTypeName(terminalType.getName());
                            DeviceType deviceType = this.deviceTypeService.selectObjById(e.getDeviceTypeId());
                            if(deviceType != null){
                                e.setDeviceTypeName(deviceType.getName());
                            }
                        }
                    });
                    flux_terminal.put("terminal", terminals);
                    // 流量
                    Map flux = new HashMap();
                    params.clear();
                    NetworkElement ne = this.networkElementService.selectObjByUuid(uuid);
                    if(ne != null){
                        args.put("ip", ne.getIp());
                        // 采集ifbasic,然后查询端口对应的历史流量
                        args.put("tag", "ifreceived");
                        args.put("available", 1);
                        List<Item> items = this.itemService.selectTagByMap(args);
                        //  Map ele = new HashMap();
                        if(items.size() > 0){
                            for (Item item : items) {
                                String lastvalue = this.zabbixService.getItemLastvalueByItemId(item.getItemid().intValue());
                                flux.put("received", lastvalue);
                                break;
                            }
                        } else{
                            flux.put("received", "0");
                        }
                        args.clear();
                        args.put("ip", ne.getIp());
                        // 采集ifbasic,然后查询端口对应的历史流量
                        args.put("tag", "ifsent");
                        args.put("available", 1);
                        List<Item> ifsents = this.itemService.selectTagByMap(args);
                        if(ifsents.size() > 0){
                            for (Item item : ifsents) {
                                String lastvalue = this.zabbixService.getItemLastvalueByItemId(item.getItemid().intValue());
                                flux.put("sent", lastvalue);
                                break;
                            }
                        }else{
                            flux.put("sent", "0");
                        }
                    }
                    flux_terminal.put("flux", flux);
                    result.put(uuid, flux_terminal);
                }
            }else{
                for (String item : list) {
                    args.clear();
                    args.put("uuid", item);
                    args.put("tag", "DT");
                    args.put("time", time);
                    List<Mac> macs = this.macHistoryService.selectByMap(args);
                    this.macUtil.macJoint(macs);
                    this.macUtil.writerType(macs);
                    Map flux_terminal = new HashMap();
                    flux_terminal.put("terminal", macs);
                    result.put(item, macs);
                }
            }
            rep.setNoticeType("4");
            rep.setNoticeStatus(1);
            rep.setNoticeInfo(result);
            this.redisResponseUtils.syncRedis(sessionId, result, 4);
            return rep;
        }
        rep.setNoticeType("4");
        rep.setNoticeStatus(0);
        return rep;
    }


}
