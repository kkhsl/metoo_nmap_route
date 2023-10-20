package com.metoo.nspm.core.service.nspm;

import com.metoo.nspm.entity.nspm.Res;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IResService {

    /**
     * 根据角色id查询权限集合
     * @param id
     * @return
     */
    List<Res> findResByRoleId(Long id);

    boolean delete(Long id);

}
