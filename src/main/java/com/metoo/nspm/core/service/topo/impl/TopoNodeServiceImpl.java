package com.metoo.nspm.core.service.topo.impl;

import com.metoo.nspm.core.manager.zabbix.tools.InterfaceUtil;
import com.metoo.nspm.core.service.nspm.INetworkElementService;
import com.metoo.nspm.core.service.topo.ITopoNodeService;
import com.metoo.nspm.entity.nspm.NetworkElement;
import com.metoo.nspm.entity.zabbix.Interface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TopoNodeServiceImpl implements ITopoNodeService {

    @Autowired
    private INetworkElementService networkElementService;
    @Autowired
    private InterfaceUtil interfaceUtil;

    @Override
    public List<Map> queryNetworkElement() {
        List<NetworkElement> nes = this.networkElementService.selectObjByMap(null);
        List<Map> devices = new ArrayList<>();
        if(nes.size() > 0) {
            for (NetworkElement ne : nes) {
                Interface obj = this.interfaceUtil.getInteface(ne.getIp());
                // 校验主机是否宕机
                if(obj != null){
                    Map map = new HashMap();
                    map.put("ip", ne.getIp());
                    map.put("deviceName", ne.getDeviceName());
                    map.put("uuid", ne.getUuid());
                    map.put("deviceType", ne.getVendorName());
                    map.put("type", obj.getValue());
                    devices.add(map);
                }
            }
        }
        return devices;
    }


}
