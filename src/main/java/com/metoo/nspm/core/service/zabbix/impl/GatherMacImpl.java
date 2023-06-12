package com.metoo.nspm.core.service.zabbix.impl;

import com.metoo.nspm.core.manager.admin.tools.DateTools;
import com.metoo.nspm.core.service.api.zabbix.ZabbixItemService;
import com.metoo.nspm.core.service.nspm.IMacHistoryService;
import com.metoo.nspm.core.service.nspm.IMacService;
import com.metoo.nspm.core.service.zabbix.ISuperGatherService;
import com.metoo.nspm.core.service.zabbix.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Date;

@Primary
@Service("gatherMacImpl")
public class GatherMacImpl implements ISuperGatherService {

    Logger log = LoggerFactory.getLogger(GatherMacImpl.class);

    @Autowired
    private ItemService itemService;
    @Autowired
    private ZabbixItemService zabbixItemService;
    @Autowired
    private IMacService macService;
    @Autowired
    private IMacHistoryService macHistoryService;

//    private GatherMacImpl(){}

    @Override
    public void gatherMac(Date time) {

        Long startTime = System.currentTimeMillis();
        log.info("Mac采集开始：" + DateTools.getCurrentDateByCh(startTime));
        this.itemService.gatherMacItem(time);
        log.info("Mac采集结束，采集时间为：" + (System.currentTimeMillis() - startTime) / 60 / 1000 + " 分钟"
                + (System.currentTimeMillis() - startTime) / 1000 + "秒 ");

        Long tagTime = System.currentTimeMillis();
        log.info("Mac-Tag 开始：" + DateTools.getCurrentDateByCh(tagTime));
        this.zabbixItemService.macTag();
        log.info("Mac-Tag 结束，采集时间为：" + (System.currentTimeMillis() - tagTime) / 60 / 1000 + " 分钟"
                + (System.currentTimeMillis() - tagTime) / 1000 + "秒 ");

        Long topologyTime = System.currentTimeMillis();
        log.info("Mac-Topology 开始：" + DateTools.getCurrentDateByCh(topologyTime));
        this.itemService.topologySyncToMac();
        log.info("Mac-Topology 结束，采集时间为：" + (System.currentTimeMillis() - topologyTime) / 60 / 1000 + " 分钟"
                + (System.currentTimeMillis() - topologyTime) / 1000 + "秒 ");

        Long copyTime = System.currentTimeMillis();
        log.info("Mac-copy 开始：" + DateTools.getCurrentDateByCh(copyTime));
        // 同步网元数据到Mac
        this.macService.truncateTable();
        this.macService.copyMacTemp();
        // 记录历史
        this.macHistoryService.copyMacTemp();
        log.info("Mac-copy 结束，采集时间为：" + (System.currentTimeMillis() - copyTime) / 60 / 1000 + " 分钟"
                + (System.currentTimeMillis() - copyTime) / 1000 + "秒 ");
    }
}
