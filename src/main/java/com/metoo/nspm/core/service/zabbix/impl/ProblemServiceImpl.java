package com.metoo.nspm.core.service.zabbix.impl;

import com.metoo.nspm.core.mapper.zabbix.ProblemMapper;
import com.metoo.nspm.core.service.zabbix.IProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class ProblemServiceImpl implements IProblemService {

    @Autowired
    private ProblemMapper problemMapper;


    @Override
    public int selectCount(Map params) {
        return this.problemMapper.selectCount(params);
    }

}
