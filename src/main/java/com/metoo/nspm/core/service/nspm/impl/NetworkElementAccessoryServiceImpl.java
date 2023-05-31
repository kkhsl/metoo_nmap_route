package com.metoo.nspm.core.service.nspm.impl;

import com.metoo.nspm.core.mapper.nspm.NetworkElementAccessoryMapper;
import com.metoo.nspm.core.service.nspm.INetworkElementAccessoryService;
import com.metoo.nspm.entity.nspm.NeAccessory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

@Service
@Transactional
public class NetworkElementAccessoryServiceImpl implements INetworkElementAccessoryService {

    @Resource
    private NetworkElementAccessoryMapper networkElementAccessoryMapper;

    @Override
    public int save(NeAccessory instance) {
        return this.networkElementAccessoryMapper.save(instance);
    }

    @Override
    public NeAccessory selectObjByMap(Map params) {
        return this.networkElementAccessoryMapper.selectObjByMap(params);
    }

    @Override
    public NeAccessory selectObjById(Long id) {
        return this.networkElementAccessoryMapper.selectObjById(id);
    }

    @Override
    public int delete(Long id) {
        return this.networkElementAccessoryMapper.delete(id);
    }
}
