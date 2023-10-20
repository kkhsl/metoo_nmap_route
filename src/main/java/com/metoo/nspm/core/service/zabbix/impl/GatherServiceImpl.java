package com.metoo.nspm.core.service.zabbix.impl;

import com.metoo.nspm.core.service.nspm.IRoutHistoryService;
import com.metoo.nspm.core.service.nspm.IRoutService;
import com.metoo.nspm.core.service.zabbix.IGatherService;
import com.metoo.nspm.core.service.zabbix.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class GatherServiceImpl implements IGatherService {

    Logger log = LoggerFactory.getLogger(GatherServiceImpl.class);

    @Autowired
    private ItemService itemService;
    @Autowired
    private IRoutService routService;
    @Autowired
    private IRoutHistoryService routHistoryService;

    @Override
    public void gatherRouteItem(Date time) {
        this.itemService.gatherRouteItem(time);
        this.routService.truncateTable();
        this.routService.copyRoutTemp();
        this.routHistoryService.copyRoutTemp();

    }

}
