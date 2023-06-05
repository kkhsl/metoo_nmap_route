package com.metoo.nspm.core.service.nspm;

import com.github.pagehelper.Page;
import com.metoo.nspm.dto.BackupSqlDTO;
import com.metoo.nspm.entity.nspm.BackUp;
import com.metoo.nspm.entity.nspm.BackupSql;

import java.util.List;
import java.util.Map;

public interface IBackupSqlService {

    BackupSql selectObjById(Long id);

    BackupSql selectObjByName(String name);

    Page<BackupSql> selectObjConditionQuery(BackupSqlDTO dto);

    List<BackupSql> selectObjByMap(Map params);

    int save(BackupSql instance);

    int update(BackupSql instance);

    int delete(Long id);

}
