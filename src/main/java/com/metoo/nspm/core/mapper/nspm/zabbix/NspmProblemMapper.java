package com.metoo.nspm.core.mapper.nspm.zabbix;

import com.metoo.nspm.dto.NspmProblemDTO;
import com.metoo.nspm.entity.zabbix.Problem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NspmProblemMapper {

    Problem selectObjById(Long id);

    List<Problem> selectObjConditionQuery(NspmProblemDTO dto);

    List<Problem> selectObjByMap(Map params);

    int update(Problem instance);

    void truncateTable();

    void copyProblemTemp(Map params);

}
