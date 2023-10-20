package com.metoo.nspm.core.service.nspm;

import com.metoo.nspm.entity.nspm.User;

import java.util.List;
import java.util.Map;

public interface IUserService {

    /**
     * 根据Username 查询一个User 对象
     * @param username
     * @return
     */
    User findByUserName(String username);


    List<User> selectObjByMap(Map params);

    
}
