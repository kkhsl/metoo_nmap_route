package com.metoo.nspm.core.service.zabbix.impl;

import com.metoo.nspm.core.mapper.zabbix.ItemMapper;
import com.metoo.nspm.core.service.nspm.IRoutTempService;
import com.metoo.nspm.core.service.topo.ITopoNodeService;
import com.metoo.nspm.core.service.zabbix.ItemService;
import com.metoo.nspm.core.utils.network.IpUtil;
import com.metoo.nspm.core.utils.network.IpV4Util;
import com.metoo.nspm.entity.nspm.RouteTemp;
import com.metoo.nspm.entity.zabbix.Item;
import com.metoo.nspm.entity.zabbix.ItemTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
//@Transactional(rollbackFor = Exception.class)
public class ItemServiceImpl implements ItemService {

    Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ITopoNodeService topoNodeService;
    @Autowired
    private IRoutTempService routTempService;

    @Override
    public void gatherRouteItem(Date time) {
        List<Map> ipList = this.topoNodeService.queryNetworkElement();
        if(ipList != null && ipList.size() > 0) {
            this.routTempService.truncateTable();
            for (Map map : ipList) {
                String deviceName = map.get("deviceName").toString();
                String ip = map.get("ip").toString();
                String uuid = map.get("uuid").toString();
                Map params = new HashMap();
                params.clear();
                params.put("ip", ip);
                params.put("tag", "routecisco");
                params.put("index", "routeifindex");
                params.put("tag_relevance", "ifbasic");
                params.put("index_relevance", "ifindex");
                params.put("name_relevance", "ifname");
                List<Item> itemRouteciscoTagList = this.itemMapper.gatherItemByTag(params);
                if(itemRouteciscoTagList.size() > 0){
                    for (Item item : itemRouteciscoTagList) {
                        List<ItemTag> tags = item.getItemTags();
                        RouteTemp routTemp = new RouteTemp();
                        routTemp.setDeviceName(deviceName);
                        routTemp.setDeviceUuid(uuid);
                        routTemp.setAddTime(time);
                        if (tags != null && tags.size() > 0) {
                            String routedest = "";
                            for (ItemTag tag : tags) {
                                String value = tag.getValue();
                                if(tag.getTag().equals("route")){
                                    String[] str = value.split("\\.");
                                    StringBuffer dest = new StringBuffer();
                                    String mask = "";
                                    StringBuffer nexthop = new StringBuffer();
                                    Scanner sc = new Scanner(value).useDelimiter("\\.");
                                    for(int i = 1; i<= str.length; i ++){
                                        if(2 <= i && i <= 4){
                                            dest.append(sc.next()).append(".");
                                        }else if(i == 5){
                                            dest.append(sc.next());
                                        }else if(i == 6){
                                            mask  = sc.next();
                                        }else if(12 <= i && i <= 14){
                                            nexthop.append(sc.next()).append(".");
                                        }else if(i == 15){
                                            nexthop.append(sc.next());
                                        }else{
                                            sc.next();
                                        }
                                    }
                                    routedest = dest.toString();
                                    routTemp.setDestination(IpUtil.ipConvertDec(dest.toString()));
                                    if(com.metoo.nspm.core.utils.MyStringUtils.isInteger(mask)){
                                        routTemp.setMaskBit(Integer.parseInt(mask));
                                        String mk = IpUtil.bitMaskConvertMask(Integer.parseInt(mask));
                                        routTemp.setMask(mk);
                                    }
                                    routTemp.setNextHop(IpUtil.ipConvertDec(nexthop.toString()));
                                }
                                if(tag.getTag().equals("routemetric")){
                                    routTemp.setCost(value);
                                }
                                if(tag.getTag().equals("routeproto")){
                                    String v = "";
                                    switch (value){
                                        case "2":
                                            v = "direct";
                                            break;
                                        case "3":
                                            v = "static";
                                            break;
                                        case "8":
                                            v = "rip";
                                            break;
                                        case "9":
                                            v = "isis";
                                            break;
                                        case "13":
                                            v = "ospf";
                                            break;
                                        case "14":
                                            v = "bgp";
                                            break;
                                    }
                                    routTemp.setProto(v);
                                }
                                if(tag.getTag().equals("routeifindex")){
                                    routTemp.setInterfaceName(tag.getName());
                                }
                            }
                            if(routTemp.getProto().equals("2")){
                                map.put("nextHop", "");
                                map.put("flags", "D");
                            }
                            if(!routTemp.getCost().equals("{#RTMETRIC}")
                                    && !routedest.equals("127.0.0.1")){
                                this.routTempService.save(routTemp);
                            }
                        }
                    }
                }
                if(itemRouteciscoTagList.size() <= 0){
                    params.clear();
                    params.put("ip", ip);
                    params.put("tag", "route");
                    params.put("index", "routeifindex");
                    params.put("tag_relevance", "ifbasic");
                    params.put("index_relevance", "ifindex");
                    params.put("name_relevance", "ifname");
                    List<Item> itemRouteTagList = this.itemMapper.gatherItemByTag(params);
                    if (itemRouteTagList.size() > 0) {
                        for (Item item : itemRouteTagList) {
                            List<ItemTag> tags = item.getItemTags();
                            RouteTemp routTemp = new RouteTemp();
                            routTemp.setDeviceName(deviceName);
                            routTemp.setDeviceUuid(uuid);
                            routTemp.setAddTime(time);
                            String routedest = "";
                            if (tags != null && tags.size() > 0) {
                                for (ItemTag tag : tags) {
                                    String value = tag.getValue();
                                    if(tag.getTag().equals("routedest")){
                                        routedest = value;
                                        routTemp.setDestination(IpUtil.ipConvertDec(value));
                                    }
                                    if(tag.getTag().equals("routemask")){
                                        routTemp.setMask(value);
                                        routTemp.setMaskBit(IpV4Util.getMaskBitByMask(value));
                                    }
                                    if(tag.getTag().equals("routemetric")){
                                        routTemp.setCost(value);
                                    }
                                    if(tag.getTag().equals("routenexthop")){
                                        routTemp.setNextHop(IpUtil.ipConvertDec(value));
                                    }
                                    if(tag.getTag().equals("routeproto")){
                                        String v = "";
                                        switch (value){
                                            case "2":
                                                v = "direct";
                                                break;
                                            case "3":
                                                v = "static";
                                                break;
                                            case "8":
                                                v = "rip";
                                                break;
                                            case "9":
                                                v = "isis";
                                                break;
                                            case "13":
                                                v = "ospf";
                                                break;
                                            case "14":
                                                v = "bgp";
                                                break;
                                        }
                                        routTemp.setProto(v);
                                    }
                                    if(tag.getTag().equals("routeifindex")){
                                        routTemp.setInterfaceName(tag.getName());
                                    }
                                }
                                if(routTemp.getProto().equals("2")){
                                    map.put("nextHop", "");
                                    map.put("flags", "D");
                                }
                                if(!routTemp.getCost().equals("{#RTMETRIC}")
                                        && !routedest.equals("127.0.0.1")){
                                    this.routTempService.save(routTemp);
                                }
                            }
                        }
                    }
                }

            }
        }
    }

}
