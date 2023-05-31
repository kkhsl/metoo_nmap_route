package com.metoo.nspm.core.mapper.nspm;

import com.metoo.nspm.dto.DeviceTypeDTO;
import com.metoo.nspm.entity.nspm.DeviceType;
import com.metoo.nspm.vo.DeviceTypeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeviceTypeMapper {

    DeviceType selectObjById(Long id);

    DeviceType selectObjByName(String name);

    List<DeviceType> selectConditionQuery(DeviceTypeDTO dto);

    List<DeviceType> selectObjByMap(Map params);

    List<DeviceType> selectCountByLeftJoin();

    List<DeviceType> selectCountByJoin();

    List<DeviceType> selectDeviceTypeAndTerminalByJoin();

    List<DeviceType> selectDeviceTypeAndNeByJoin();

    List<DeviceType> selectNeSumByType(Map params);

    List<DeviceType> selectNeByType(Integer type);
    List<DeviceType> selectTerminalCountByJoin();

    List<DeviceType> selectTerminalSumByType(Map params);

    List<DeviceTypeVO> statistics();

    int save(DeviceType instance);

    int update(DeviceType instance);

    int delete(Long id );

    int batcheDel(Long[] ids);
}
