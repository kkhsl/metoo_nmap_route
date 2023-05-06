package com.metoo.nspm.core.service.nspm;

import com.metoo.nspm.entity.nspm.Mac;
import com.metoo.nspm.entity.nspm.MacTemp;
import com.metoo.nspm.entity.nspm.Terminal;
import com.metoo.nspm.entity.nspm.TerminalHistory;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ITerminalHistoryService {

    List<TerminalHistory> selectObjByMap(Map params);

    int save(TerminalHistory terminalHistory);

    void syncTerminalToHistory(Date time);
}
