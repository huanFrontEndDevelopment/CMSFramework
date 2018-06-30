package tv.huan.cms.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tv.huan.cms.dao.IPermissionDAO;
import tv.huan.cms.entity.Permission;
import tv.huan.cms.mapper.IPermissionMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Project Name:BasicCMS
 * File Name:PermissionDAOImpl
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:52
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Repository("permissionDAO")
public class PermissionDAOImpl implements IPermissionDAO {

    @Autowired
    private IPermissionMapper permissionMapper;

    @Override
    public JSONArray selectPermission(int offset, int pageSize, String orderColumn, String orderSort) {
        List<Permission> list = permissionMapper.selectPermission(offset, pageSize, orderColumn, orderSort);
        return (JSONArray) JSON.toJSON(list);
    }
    @Override
    public int selectPermissionCount() {
        return permissionMapper.selectPermissionCount();
    }

    @Override
    public int insertPermission(Permission permission) {
        int res = permissionMapper.insertSelective(permission);
        if(res == 1){
            return permission.getId();
        }else{
            return -1;
        }

    }

    @Override
    public int deleteByIds(String[] ids) {
        Map<String,Object> map = new HashMap<>(10);
        map.put("ids",ids);
        return permissionMapper.deleteByIdList(map);
    }

    @Override
    public int updateByPrimaryKeySelective(Permission record) {
        return permissionMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Permission selectByName(String name) {
        return permissionMapper.selectByName(name);
    }


    @Override
    public Permission selectByPrimaryKey(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }





    @Override
    public List<Permission> selectUserBindPermissionByUserName(String username) {
        return permissionMapper.selectUserBindPermissionByUserName(username);
    }

    @Override
    public List<String> selectRolePermissionUrlByRoleId(String roleId) {
        return permissionMapper.selectRolePermissionUrlByRoleId(roleId);
    }


}
