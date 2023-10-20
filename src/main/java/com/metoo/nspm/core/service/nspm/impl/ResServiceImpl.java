package com.metoo.nspm.core.service.nspm.impl;

import com.metoo.nspm.core.mapper.nspm.ResMapper;
import com.metoo.nspm.core.service.nspm.IResService;
import com.metoo.nspm.entity.nspm.Res;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service("resService")
@Transactional
public class ResServiceImpl implements IResService {

    @Autowired
    private ResMapper resMapper;

    @Override
    public List<Res> findResByRoleId(Long id) {
        return this.resMapper.findResByRoleId(id);
    }

    @Override
    public boolean delete(Long id) {
        try {
            this.resMapper.delete(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
