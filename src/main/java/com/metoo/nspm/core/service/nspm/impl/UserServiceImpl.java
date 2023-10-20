package com.metoo.nspm.core.service.nspm.impl;

import com.metoo.nspm.core.mapper.nspm.UserMapper;
import com.metoo.nspm.core.service.nspm.IRoleService;
import com.metoo.nspm.core.service.nspm.IUserRoleService;
import com.metoo.nspm.core.service.nspm.IUserService;
import com.metoo.nspm.entity.nspm.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserName(String username) {
        return this.userMapper.findByUserName(username);
    }


    @Override
    public List<User> selectObjByMap(Map params) {
        return this.userMapper.selectObjByMap(params);
    }

}
