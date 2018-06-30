package tv.huan.cms.dao;

import com.alibaba.fastjson.JSONArray;
import tv.huan.cms.entity.Permission;

import java.util.List;

/**
 * Project Name:BasicCMS
 * File Name:IPermissionDAO
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:52
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
public interface IPermissionDAO {

    /**
     * 分页获取权限list
     * @param offset 起始位置
     * @param pageSize 偏移量
     * @param orderColumn 排序字段
     * @param orderSort 排序方式
     * @return permission list
     */
    JSONArray selectPermission(int offset, int pageSize, String orderColumn, String orderSort);
    /**
     * 获取权限列表长度
     * @return permission list total count
     */
    int selectPermissionCount();

    /**
     * 添加权限
     * @param permission 添加权限对象
     * @return 添加权限id
     */
    int insertPermission(Permission permission);

    /**
     * 删除权限
     * @param ids 删除权限id数组
     * @return 删除数量
     */
    int deleteByIds(String[] ids);

    /**
     * 更新权限
     * @param permission 权限对象
     * @return 1:更新成功
     */
    int updateByPrimaryKeySelective(Permission permission);

    /**
     * 根据权限名查询权限
     * @param name 权限名
     * @return name对应权限对象
     */
    Permission selectByName(String name);

    /**
     * 根据username获取用户权限
     * @param username 用户名
     * @return 权限列表
     */
    List<Permission> selectUserBindPermissionByUserName(String username);

    /**
     * 根据用户角色id获取用户角色所绑定权限的权限url
     * @param roleId 用户角色id
     * @return 权限permission url列表
     */
    List<String> selectRolePermissionUrlByRoleId(String roleId);



    Permission selectByPrimaryKey(Integer id);










}
