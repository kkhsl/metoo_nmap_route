package com.metoo.nspm.core.service.nspm;

import com.metoo.nspm.entity.nspm.NetworkElement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface INetworkElementService {


    List<NetworkElement> selectObjByMap(Map params);


}
