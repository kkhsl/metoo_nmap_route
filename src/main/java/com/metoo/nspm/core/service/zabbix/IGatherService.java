package com.metoo.nspm.core.service.zabbix;

import java.util.Date;

public interface IGatherService {

    /**
     * 采集Arp
     * @param time
     */
    void gatherArpItem(Date time);

    /**
     * 采集Mac
     * 遍历网元采集，单条数据（item_tag）插入
     * @param time
     */
    void gatherMacItem(Date time);


    /**
     *  Stream 批量处理
     * List集合存储采集数据，使用Mybatis批量插入提高效率（目前数据量为1w左右）
     * @param time
     * @throws InterruptedException
     */
    void gatherMacBatch(Date time) throws InterruptedException;


    /**
     * 使用Stream，优化大数据量下的for循环
     * @param time
     */
    void gatherMacBatchStream(Date time);

    /**
     * 使用线程池
     * @param time
     * @throws InterruptedException
     */
    // 线程不安全
    void gatherMacThreadPool(Date time);
    // 线程不安全
    void gatherMacThreadPool2(Date time) throws InterruptedException;
    // 线程不安全
    void gatherMacThreadPool3(Date time);
    // 好像安全
    void gatherMacThreadPool4(Date time);

    /**
     * 采集路由
     * @param time
     */
    void gatherRouteItem(Date time);

    /**
     * 采集Ip地址
     * @param time
     */
    void gatherIpaddressItem(Date time);

    /**
     * 采集告警信息
     * @param time
     */
    void gatherProblemItem(Date time);

    /**
     * 采集主机接口（主机状态）
     */
//    void gatherInterfaceitem(Date time);

    void testTransactional();

    /**
     * 采集主机状态
     */
//    void gatherSnmpAvailable();

    void gatherSpanningTreeProtocol(Date time);

    // 采集主机的snmp状态
    void gatherHostSnmp(Date time);

}
