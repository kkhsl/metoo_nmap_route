package com.metoo.nspm.core.mapper.nspm;

import com.metoo.nspm.entity.nspm.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {

    /**
     *根据用户id查询用所有角色信息
     * @param user_id
     * @return
     */
    List<Role> findRoleByUserId(Long user_id);

    int insert(Role instance);

    int update(Role instance);

    int delete(Long id);

}
