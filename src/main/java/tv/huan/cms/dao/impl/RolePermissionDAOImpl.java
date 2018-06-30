package tv.huan.cms.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tv.huan.cms.dao.IRolePermissionDAO;
import tv.huan.cms.entity.RolePermission;
import tv.huan.cms.mapper.IRolePermissionMapper;

import java.util.List;

/**
 * Project Name:BasicCMS
 * File Name:RolePermissionDAOImpl
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:55
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Repository("rolePermissionDAO")
public class RolePermissionDAOImpl implements IRolePermissionDAO {

    @Autowired
    private IRolePermissionMapper rolePermissionMapper;

    @Override
    public JSONArray selectRolePermission(int offset, int pageSize, String orderColumn, String orderSort) {
        List<RolePermission> list = rolePermissionMapper.selectRolePermission(offset,pageSize,orderColumn,orderSort);
        return (JSONArray) JSON.toJSON(list);
    }

    @Override
    public int selectTotalCount() {
        return rolePermissionMapper.selectTotalCount();
    }

    @Override
    public int insert(RolePermission rolePermission) {
        int res = rolePermissionMapper.insert(rolePermission);
        if(res == 1){
            return rolePermission.getId();
        }else{
            return -1;
        }
    }

    @Override
    public int updateByPrimaryKeySelective(RolePermission rolePermission) {
        return rolePermissionMapper.updateByPrimaryKeySelective(rolePermission);
    }

    @Override
    public int deleteByIds(String[] ids) {
        return rolePermissionMapper.deleteByIds(ids);
    }

    @Override
    public int batchInsert(String roleId, String[] permissionIds) {
        return rolePermissionMapper.batchInsert(roleId,permissionIds);
    }

    @Override
    public RolePermission selectByPrimaryKey(Integer id) {
        return rolePermissionMapper.selectByPrimaryKey(id);
    }




}

