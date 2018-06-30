package tv.huan.cms.service;

import tv.huan.cms.entity.User;

/**
 * Project Name:BasicCMS
 * File Name:UserLoginService
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:35
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
public interface UserLoginService {
    /**
     * 检查用户名密码
     * @param uname 用户名
     * @param upasswd 密码
     * @return true/false
     */
    boolean checkUserExist(String uname, String upasswd);

    /**
     * 根据用户名获取用户对象
     * @param uname 用户名
     * @return User用户对象
     */
    User getUserByUserName(String uname);
}
