package com.metoo.nspm.core.service.nspm.impl;

import com.metoo.nspm.core.mapper.nspm.NetworkElementMapper;
import com.metoo.nspm.core.service.nspm.INetworkElementService;
import com.metoo.nspm.entity.nspm.NetworkElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class NetworkElementServiceImpl implements INetworkElementService {

    @Autowired
    private NetworkElementMapper networkElementMapper;

    @Override
    public List<NetworkElement> selectObjByMap(Map params) {

        return this.networkElementMapper.selectObjByMap(params);
    }

}
