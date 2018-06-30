package tv.huan.cms.shiro;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import tv.huan.cms.entity.Permission;
import tv.huan.cms.entity.Role;
import tv.huan.cms.entity.User;
import tv.huan.cms.service.AccountService;
import tv.huan.cms.service.AuthorizationService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Project Name:BasicCMS
 * File Name:UserRealm
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:59
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AuthorizationService authorizationService;

    /***
     * 获取授权信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String adminFlag = "root";
        String username = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> permissionNameList = new HashSet<String>();
        if(adminFlag.equals(username)){
            JSONObject permJson = authorizationService.getList(0,0,null,null,null,Permission.class);
            JSONArray permArray = permJson.getJSONArray("rows");
            for(Object permission : permArray){
                JSONObject obj = (JSONObject) permission;
                permissionNameList.add(obj.getString("expression"));
            }
        }else{
            List<Permission> permissionList = accountService.getPermissionByUserName(username);
            for(Permission permission : permissionList){
                permissionNameList.add(permission.getExpression());
            }
        }
        Set<String> roleNameList = new HashSet<>();
        List<Role> roleList = accountService.getRoleByUserName(username);
        for(Role role : roleList){
            roleNameList.add(role.getName());
        }
        authorizationInfo.setRoles(roleNameList);
        authorizationInfo.setStringPermissions(permissionNameList);
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken at) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) at;
        String username = token.getUsername();
        if (username != null && !"".equals(username)) {
            User user = accountService.getUserByUserName(username);
            if(user.getIsDelete() == 0){
                //用户被锁定
                log.info("用户:"+user.getUserName()+"已被锁定 ");
                throw new LockedAccountException();
            }
            if(user != null){
                return new SimpleAuthenticationInfo(user.getUserName(),user.getPassword(), ByteSource.Util.bytes(user.getCredentialsSalt()),getName());
            }else{
                // 用户名不存在抛出异常
                throw new UnknownAccountException();
            }
        }else{
            // 用户名不存在抛出异常
            throw new UnknownAccountException();
        }
    }
}

