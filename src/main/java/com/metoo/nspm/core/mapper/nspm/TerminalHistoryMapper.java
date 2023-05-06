package com.metoo.nspm.core.mapper.nspm;

import com.metoo.nspm.entity.nspm.TerminalHistory;

import java.util.List;
import java.util.Map;

public interface TerminalHistoryMapper {

    List<TerminalHistory> selectObjByMap(Map params);

    int save(TerminalHistory terminalHistory);
}
