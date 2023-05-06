package com.metoo.nspm.core.service.zabbix.impl;

import com.metoo.nspm.core.mapper.zabbix.InterfaceMapper;
import com.metoo.nspm.core.service.zabbix.InterfaceService;
import com.metoo.nspm.entity.zabbix.Interface;
import com.metoo.nspm.entity.zabbix.InterfaceTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class InterfaceServiceImpl implements InterfaceService {

    @Autowired
    private InterfaceMapper interfaceMapper;

    @Override
    public Interface selectObjByIp(String ip) {
        return this.interfaceMapper.selectObjByIp(ip);
    }

    @Override
    public Interface selectInfAndTag(String ip) {
        return this.interfaceMapper.selectInfAndTag(ip);
    }

    @Override
    public Map getHostTag(String ip) {
        Map map = new HashMap();
        String vendor = "";// H3C
        String model = "";// S10508
        Interface anInterface = this.selectInfAndTag(ip);
        if(anInterface == null){
            return map;
        }
        for (InterfaceTag itemTag : anInterface.getItemTags()) {
            if(itemTag.getTag().equals("vender")){
                vendor = itemTag.getValue();
            }
            if(itemTag.getTag().equals("model")){
                model = itemTag.getValue();
            }
        }

        map.put("vendor", vendor);
        map.put("model", model);
        return map;
    }
}
