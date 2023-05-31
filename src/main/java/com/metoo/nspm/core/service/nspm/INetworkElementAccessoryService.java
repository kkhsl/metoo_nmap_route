package com.metoo.nspm.core.service.nspm;

import com.metoo.nspm.core.mapper.nspm.NetworkElementAccessoryMapper;
import com.metoo.nspm.entity.nspm.NeAccessory;
import com.metoo.nspm.entity.nspm.NetworkElement;

import java.util.Map;

public interface INetworkElementAccessoryService {

    int save(NeAccessory instance);

    NeAccessory selectObjByMap(Map params);

    NeAccessory selectObjById(Long id);

    int delete(Long id);
}
