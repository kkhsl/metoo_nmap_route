package com.metoo.nspm.core.service.zabbix;

import com.metoo.nspm.entity.zabbix.Interface;

import java.util.Map;

public interface InterfaceService {

    Interface selectObjByIp(String ip);
    Interface selectInfAndTag(String ip);
    Map getHostTag(String ip);
}
