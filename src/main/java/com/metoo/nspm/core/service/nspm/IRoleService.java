package com.metoo.nspm.core.service.nspm;

import com.metoo.nspm.entity.nspm.Role;

import java.util.List;
import java.util.Map;

public interface IRoleService {


    /**
     *根据用户id查询用所有角色信息
     * @param user_id
     * @return
     */
    List<Role> findRoleByUserId(Long user_id);

    boolean delete(Long id);

}
