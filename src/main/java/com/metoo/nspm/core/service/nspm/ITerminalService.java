package com.metoo.nspm.core.service.nspm;

import com.github.pagehelper.Page;
import com.metoo.nspm.dto.RsmsDeviceDTO;
import com.metoo.nspm.dto.TerminalDTO;
import com.metoo.nspm.entity.nspm.Mac;
import com.metoo.nspm.entity.nspm.RsmsDevice;
import com.metoo.nspm.entity.nspm.Terminal;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ITerminalService {

    Terminal selectObjById(Long id);

    Page<Terminal> selectConditionQuery(TerminalDTO instance);

    List<Terminal> selectObjByMap(Map params);

    int save(Terminal instance);

    int update(Terminal instance);

    int delete(Long id);

    int batchInert(List<Terminal> instances);

    int batchUpdate(List<Terminal> instances);

    // 同步DT信息
    void syncMacDtToTerminal();

    // 同步终端类型
    void syncHistoryMac(Date time);

    // 改为资产终端
    int editTerminalType(Long[] ids);
}
