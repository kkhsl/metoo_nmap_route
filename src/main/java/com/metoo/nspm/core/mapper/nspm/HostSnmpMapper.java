package com.metoo.nspm.core.mapper.nspm;

import com.metoo.nspm.entity.nspm.HostSnmp;

import java.util.List;
import java.util.Map;

public interface HostSnmpMapper {
    List<HostSnmp> selectObjByMap(Map params);
    int save(HostSnmp instance);
    int update(HostSnmp instance);
}
