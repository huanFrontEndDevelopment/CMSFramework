package tv.huan.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tv.huan.cms.dao.IPermissionDAO;
import tv.huan.cms.dao.IRoleDAO;
import tv.huan.cms.dao.IUserDAO;
import tv.huan.cms.entity.Permission;
import tv.huan.cms.entity.Role;
import tv.huan.cms.entity.User;
import tv.huan.cms.service.AccountService;

import java.util.List;

/**
 * Project Name:BasicCMS
 * File Name:AccountServiceImpl
 *
 * @author wangyuxi
 * @date 2018/6/6 下午6:05
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private IUserDAO userDAO;
    @Autowired
    private IRoleDAO roleDAO;
    @Autowired
    private IPermissionDAO permissionDAO;

    @Override
    public User getUserByUserName(String username) {
        return userDAO.selectUserByUserName(username);
    }

    @Override
    public List<Permission> getPermissionByUserName(String username) {
        return permissionDAO.selectUserBindPermissionByUserName(username);
    }

    @Override
    public List<Role> getRoleByUserName(String username) {
        return roleDAO.selectRoleByUserName(username);
    }

    @Override
    public List<String> getPermissionsByUserName(String username) {
        try {
            User user = getUserByUserName(username);
            if (user == null) {
                return null;
            }

        }catch (Exception e){
            log.error("获取用户权限service出错",e);
        }
        return null;
    }




}

