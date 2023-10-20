package com.metoo.nspm.core.service.nspm.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.metoo.nspm.core.mapper.nspm.RoleMapper;
import com.metoo.nspm.core.service.nspm.IResService;
import com.metoo.nspm.core.service.nspm.IRoleResService;
import com.metoo.nspm.core.service.nspm.IRoleService;
import com.metoo.nspm.entity.nspm.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
@Transactional
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> findRoleByUserId(Long user_id) {
        return this.roleMapper.findRoleByUserId(user_id);
    }

    @Override
    public boolean delete(Long id) {
        int flag = this.roleMapper.delete(id);
        return flag != 0;
    }

}
