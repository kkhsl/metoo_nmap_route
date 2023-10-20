package com.metoo.nspm.core.service.zabbix;

import java.util.Date;

public interface IGatherService {

    /**
     * 采集路由
     * @param time
     */
    void gatherRouteItem(Date time);

}
