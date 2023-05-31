package com.metoo.nspm.core.mapper.nspm;

import com.metoo.nspm.entity.nspm.ConfigBackup;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConfigBackupMapper {

    ConfigBackup getInstance();
}
