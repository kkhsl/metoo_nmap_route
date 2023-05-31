package com.metoo.nspm.core.service.nspm;

import com.metoo.nspm.core.utils.ResponseUtil;
import com.metoo.nspm.core.websocket.api.NetWorkManagerApi;
import com.metoo.nspm.dto.NetworkElementDto;
import com.metoo.nspm.entity.nspm.NetworkElement;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import sun.nio.ch.Net;

import java.util.List;
import java.util.Map;

@Mapper
public interface INetworkElementService {

    NetworkElement selectObjById(Long id);

    NetworkElement selectObjByUuid(String uuid);

    Page<NetworkElement> selectConditionQuery(NetworkElementDto instance);

    List<NetworkElement> selectObjByMap(Map params);

    List <NetworkElement> selectObjAll();

    int save(NetworkElement instance);

    int batchInsert(List<NetworkElement> instance);

    int update(NetworkElement instance);

    int del(Long id);

    Object backup(NetworkElement instance);

    NetworkElement selectAccessoryByUuid(String uuid);

    Object delConfig(Long id);

}
