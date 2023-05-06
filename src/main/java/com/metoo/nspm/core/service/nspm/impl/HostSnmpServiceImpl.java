package com.metoo.nspm.core.service.nspm.impl;

import com.metoo.nspm.core.mapper.nspm.HostSnmpMapper;
import com.metoo.nspm.core.service.nspm.IHostSnmpService;
import com.metoo.nspm.entity.nspm.HostSnmp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class HostSnmpServiceImpl implements IHostSnmpService {

    @Resource
    private HostSnmpMapper hostSnmpMapper;

    @Override
    public List<HostSnmp> selectObjByMap(Map params) {
        return this.hostSnmpMapper.selectObjByMap(params);
    }

    @Override
    public int save(HostSnmp instance) {
        try {
            return this.hostSnmpMapper.save(instance);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(HostSnmp instance) {
        return 0;
    }
}
