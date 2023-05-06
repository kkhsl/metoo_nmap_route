package com.metoo.nspm.core.service.nspm.impl;

import com.metoo.nspm.core.mapper.nspm.TerminalHistoryMapper;
import com.metoo.nspm.core.mapper.nspm.TerminalMapper;
import com.metoo.nspm.core.service.nspm.ITerminalHistoryService;
import com.metoo.nspm.core.service.nspm.ITerminalService;
import com.metoo.nspm.entity.nspm.Mac;
import com.metoo.nspm.entity.nspm.Terminal;
import com.metoo.nspm.entity.nspm.TerminalHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TerminalHistoryServiceImpl implements ITerminalHistoryService {

    @Resource
    private TerminalHistoryMapper terminalHistoryMapper;
    @Autowired
    private ITerminalService terminalService;

    @Override
    public List<TerminalHistory> selectObjByMap(Map params) {
        return this.terminalHistoryMapper.selectObjByMap(params);
    }

    @Override
    public int save(TerminalHistory terminalHistory) {
        try {
            return this.terminalHistoryMapper.save(terminalHistory);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void syncTerminalToHistory(Date time) {
        List<Terminal> terminals = this.terminalService.selectObjByMap(null);
        if(terminals.size() > 0){
            for (Terminal terminal : terminals) {
                TerminalHistory ts = new TerminalHistory();
                ts.setAddTime(time);
                ts.setIp(terminal.getIp());
                ts.setOnline(terminal.getOnline());
                this.terminalHistoryMapper.save(ts);
            }
        }
    }
}
