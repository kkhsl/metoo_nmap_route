package com.metoo.nspm.core.service.nspm;

import com.metoo.nspm.entity.nspm.RouteTemp;

import java.util.List;
import java.util.Map;

public interface IRoutTempService {


    List<RouteTemp> selectObjByMap(Map params);

    int save(RouteTemp instance);

    int update(RouteTemp instance);

    int delete(Long id);

    void truncateTable();

}
