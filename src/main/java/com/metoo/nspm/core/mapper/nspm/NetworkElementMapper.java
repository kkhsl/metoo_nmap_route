package com.metoo.nspm.core.mapper.nspm;

import com.metoo.nspm.entity.nspm.NetworkElement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NetworkElementMapper {

    List<NetworkElement> selectObjByMap(Map params);


}
