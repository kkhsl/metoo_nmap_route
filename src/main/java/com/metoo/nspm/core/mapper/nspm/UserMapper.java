package com.metoo.nspm.core.mapper.nspm;

import com.metoo.nspm.entity.nspm.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {

    /**
     * 根据Username 查询一个User 对象
     * @param username
     * @return
     */
    User findByUserName(String username);

    List<User> selectObjByMap(Map params);


}
