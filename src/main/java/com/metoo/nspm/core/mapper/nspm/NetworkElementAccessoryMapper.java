package com.metoo.nspm.core.mapper.nspm;

import com.metoo.nspm.entity.nspm.NeAccessory;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface NetworkElementAccessoryMapper {

    int save(NeAccessory instance);

    NeAccessory selectObjByMap(Map params);

    NeAccessory selectObjById(Long id);

    int delete(Long id);
}
