package com.metoo.nspm.core.mapper.nspm.zabbix;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ZabbixRouteMapper {

    void truncateTable();

    void copyRoutTemp();

}
