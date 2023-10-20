package com.metoo.nspm.core.mapper.nspm;

import com.metoo.nspm.entity.nspm.Res;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mapper
public interface ResMapper {

    /**
     * 根据角色id查询权限集合
     * @param id
     * @return
     */
    List<Res> findResByRoleId(Long id);

    /**
     * 更新一个系统资源对象
     * @param instance
     * @return
     */
    int update(Res instance);

    /**
     * 根据系统资源ID删除一个系统资源对象
     * @param id
     * @return
     */
    int delete(Long id);

}
