package com.metoo.nspm.core.mapper.nspm;

import com.github.pagehelper.Page;
import com.metoo.nspm.dto.TerminalDTO;
import com.metoo.nspm.entity.nspm.Terminal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TerminalMapper {

    Terminal selectObjById(Long id);

    Page<Terminal> selectConditionQuery(TerminalDTO instance);

    List<Terminal> selectObjByMap(Map params);

    int insert(Terminal instance);

    int update(Terminal instance);

    int batchInert(List<Terminal> instances);

    int batchUpdate(List<Terminal> instances);

    int delete(Long id);
}
