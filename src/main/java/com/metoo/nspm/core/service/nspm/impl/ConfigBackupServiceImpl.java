package com.metoo.nspm.core.service.nspm.impl;

import com.metoo.nspm.core.mapper.nspm.ConfigBackupMapper;
import com.metoo.nspm.core.service.nspm.IConfigBackupService;
import com.metoo.nspm.entity.nspm.ConfigBackup;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ConfigBackupServiceImpl implements IConfigBackupService {

    @Resource
    private ConfigBackupMapper configBackupMapper;

    @Override
    public ConfigBackup getInstance() {
        return this.configBackupMapper.getInstance();
    }
}
