package com.metoo.nspm.core.service.nspm;

import com.metoo.nspm.entity.nspm.HostSnmp;

import java.util.List;
import java.util.Map;

public interface IHostSnmpService {

    List<HostSnmp> selectObjByMap(Map params);

    int save(HostSnmp instance);
    int update(HostSnmp instance);
}
