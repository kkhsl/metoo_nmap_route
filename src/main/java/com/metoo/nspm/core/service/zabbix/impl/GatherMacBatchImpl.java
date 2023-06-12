package com.metoo.nspm.core.service.zabbix.impl;

import com.metoo.nspm.core.service.api.zabbix.ZabbixItemService;
import com.metoo.nspm.core.service.nspm.IMacHistoryService;
import com.metoo.nspm.core.service.nspm.IMacService;
import com.metoo.nspm.core.service.zabbix.ISuperGatherService;
import com.metoo.nspm.core.service.zabbix.ItemService;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service("gatherMacBatchImpl")
public class GatherMacBatchImpl implements ISuperGatherService {

    Logger log = LoggerFactory.getLogger(GatherMacBatchImpl.class);

    @Autowired
    private ItemService itemService;
    @Autowired
    private ZabbixItemService zabbixItemService;
    @Autowired
    private IMacService macService;
    @Autowired
    private IMacHistoryService macHistoryService;

//    private GatherMacBatchImpl(){}


    @Override
    public void gatherMac(Date time) {
        StopWatch watch = new StopWatch();
        watch.start();
        this.itemService.gatherMacBatch(time);
        watch.stop();
        log.info("Mac采集耗时：" + watch.getTime(TimeUnit.SECONDS) + "秒.");

        watch.reset();
        watch.start();
        this.zabbixItemService.labelTheMac(time);
        watch.stop();
        log.info("Mac-tag采集耗时：" + watch.getTime(TimeUnit.SECONDS) + "秒.");

        watch.reset();
        watch.start();
        this.itemService.topologySyncToMacBatch(time);
        watch.stop();
        log.info("Mac-topology采集耗时：" + watch.getTime(TimeUnit.SECONDS) + "秒.");

        watch.reset();
        watch.start();
        // 同步网元数据到Mac
        this.macService.truncateTable();
        this.macService.copyMacTemp();
        // 记录历史
        this.macHistoryService.copyMacTemp();
        watch.stop();
        log.info("Mac-copy采集耗时：" + watch.getTime(TimeUnit.SECONDS) + "秒.");
    }
}
