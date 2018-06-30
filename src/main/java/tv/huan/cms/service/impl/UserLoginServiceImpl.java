package tv.huan.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tv.huan.cms.dao.IUserDAO;
import tv.huan.cms.entity.User;
import tv.huan.cms.service.UserLoginService;
import tv.huan.cms.util.MD5;

/**
 * Project Name:BasicCMS
 * File Name:UserLoginServiceImpl
 *
 * @author wangyuxi
 * @date 2018/6/7 上午10:00
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Slf4j
@Service("userLoginService")
public class UserLoginServiceImpl implements UserLoginService {


    @Autowired
    private IUserDAO iUserDAO;


    @Override
    public boolean checkUserExist(String uname, String upasswd) {
        try {
            User user = this.getUserByUserName(uname);
            if (user == null){
                log.debug("逻辑处理数据库根据uname获取user为空,返回false");
                return false;
            }else{
                String storePass = user.getPassword();
                upasswd = MD5.MD5Encode(upasswd).toUpperCase();
                if(storePass.equals(upasswd)){
                    log.debug("数据库中用户密码与参数一致,返回登录确认");
                    return true;
                }else{
                    log.debug("数据库中用户密码与参数不一致,返回登录失败");
                    return false;
                }
            }
        }catch (Exception e){
            log.error("逻辑判断用户登录信息出错",e);
            return false;
        }
    }

    @Override
    public User getUserByUserName(String uname) {
        return iUserDAO.selectUserByUserName(uname);
    }


}

