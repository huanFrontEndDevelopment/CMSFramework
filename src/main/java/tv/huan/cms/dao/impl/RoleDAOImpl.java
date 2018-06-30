package tv.huan.cms.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tv.huan.cms.dao.IRoleDAO;
import tv.huan.cms.entity.Role;
import tv.huan.cms.mapper.IRoleMapper;

import java.util.List;

/**
 * Project Name:BasicCMS
 * File Name:RoleDAOImpl
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:42
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Repository("roleDAO")
public class RoleDAOImpl implements IRoleDAO {
    @Autowired
    private IRoleMapper roleMapper;

    @Override
    public JSONArray selectRole(int offset, int pageSize, String orderColumn, String orderSort) {
        List<Role> list = roleMapper.selectRole(offset, pageSize, orderColumn, orderSort);
        return (JSONArray) JSON.toJSON(list);
    }
    @Override
    public int selectRoleCount() {
        return roleMapper.selectRoleCount();
    }

    @Override
    public Role selectRoleByRoleName(String roleName) {
        return roleMapper.selectRoleByRoleName(roleName);
    }
    @Override
    public int deleteByIdArray(String[] ids){
        return roleMapper.deleteByIdList(ids);
    }

    @Override
    public int insertSelective(Role role) {
        return roleMapper.insertSelective(role);
    }






    @Override
    public Role selectByPrimaryKey(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Role record) {
        return roleMapper.updateByPrimaryKeySelective(record);
    }


    @Override
    public List<Role> selectRoleByUserName(String userName) {
        return roleMapper.selectRoleByUserName(userName);
    }




}

