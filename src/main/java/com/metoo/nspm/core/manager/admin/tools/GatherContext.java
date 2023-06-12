package com.metoo.nspm.core.manager.admin.tools;

import com.metoo.nspm.core.service.zabbix.ISuperGatherService;

import java.util.Date;

public class GatherContext {

    private ISuperGatherService gatherService;

    public GatherContext(ISuperGatherService gatherService){
        this.gatherService = gatherService;
    }

    public void gatherMac(Date time){
        this.gatherService.gatherMac(time);
    }


}
