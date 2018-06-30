package tv.huan.cms.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tv.huan.cms.dao.IUserRoleDAO;
import tv.huan.cms.entity.Role;
import tv.huan.cms.entity.User;
import tv.huan.cms.entity.UserRole;
import tv.huan.cms.mapper.IUserRoleMapper;

import java.util.List;

/**
 * Project Name:BasicCMS
 * File Name:UserRoleDAOImpl
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:47
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Repository("userRoleDAO")
public class UserRoleDAOImpl implements IUserRoleDAO {

    @Autowired
    private IUserRoleMapper userRoleMapper;

    @Override
    public JSONArray selectUserRoleList(int offset, int pageSize, String orderColumn, String orderSort) {
        List<UserRole> list = userRoleMapper.selectUserRoleList(offset,pageSize,orderColumn,orderSort);
        return (JSONArray) JSON.toJSON(list);
    }

    @Override
    public int selectUserRoleCount() {
        return userRoleMapper.selectUserRoleCount();
    }

    @Override
    public int insertSelective(String userId,  String roleId) {
        User user = new User();
        Role role = new Role();
        user.setId(Integer.parseInt(userId));
        role.setId(Integer.parseInt(roleId));
        UserRole userRole = new UserRole(null,user,role);
        int res = userRoleMapper.insertSelective(userRole);
        if(res != 0){
            return userRole.getId();
        }else{
            return -1;
        }
    }

    @Override
    public int deleteByIds(String[] ids) {
        return userRoleMapper.deleteByIdList(ids);
    }

    @Override
    public int updateByPrimaryKeySelective(UserRole userRole) {

        return userRoleMapper.updateUserRole(userRole.getId(),userRole.getUser().getId(),userRole.getRole().getId());
    }



}

