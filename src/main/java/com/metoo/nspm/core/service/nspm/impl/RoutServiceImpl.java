package com.metoo.nspm.core.service.nspm.impl;

import com.metoo.nspm.core.mapper.nspm.zabbix.ZabbixRouteMapper;
import com.metoo.nspm.core.service.nspm.IRoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoutServiceImpl implements IRoutService {

    @Autowired
    private ZabbixRouteMapper zabbixRoutMapper;

    @Override
    public void truncateTable() {
        this.zabbixRoutMapper.truncateTable();
    }

    @Override
    public void copyRoutTemp() {
        this.zabbixRoutMapper.copyRoutTemp();
    }

}
