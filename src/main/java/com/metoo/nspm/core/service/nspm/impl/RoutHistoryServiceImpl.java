package com.metoo.nspm.core.service.nspm.impl;

import com.metoo.nspm.core.mapper.nspm.zabbix.RouteHistoryMapper;
import com.metoo.nspm.core.service.nspm.IRoutHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoutHistoryServiceImpl implements IRoutHistoryService {

    @Autowired
    private RouteHistoryMapper routHistoryMapper;

    @Override
    public void copyRoutTemp() {
        this.routHistoryMapper.copyRoutTemp();
    }

}
