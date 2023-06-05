package com.metoo.nspm.core.mapper.nspm;

import com.metoo.nspm.dto.BackupSqlDTO;
import com.metoo.nspm.entity.nspm.BackupSql;

import java.util.List;
import java.util.Map;

public interface BackupSqlMapper {

    BackupSql selectObjById(Long id);

    BackupSql selectObjByName(String name);

    List<BackupSql> selectObjConditionQuery(BackupSqlDTO dto);

    List<BackupSql> selectObjByMap(Map params);

    int save(BackupSql instance);

    int update(BackupSql instance);

    int delete(Long id);
}
