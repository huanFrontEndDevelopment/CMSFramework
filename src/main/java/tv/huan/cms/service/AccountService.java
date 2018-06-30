package tv.huan.cms.service;

import tv.huan.cms.entity.Permission;
import tv.huan.cms.entity.Role;
import tv.huan.cms.entity.User;

import java.util.List;

/**
 * Project Name:BasicCMS
 * File Name:AccountService
 *
 * @author wangyuxi
 * @date 2018/6/6 下午6:02
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
public interface AccountService {
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    User getUserByUserName(String username);

    /**
     * 根据用户名查询用户权限列表
     * @param username 用户名
     * @return 权限列表
     */
    List<Permission> getPermissionByUserName(String username);

    /**
     * 根据用户名查询角色列表
     * @param username 用户名
     * @return 角色列表
     */
    List<Role> getRoleByUserName(String username);

    List<String> getPermissionsByUserName(String username);




}

