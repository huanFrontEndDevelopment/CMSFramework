package tv.huan.cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tv.huan.cms.dao.*;
import tv.huan.cms.entity.*;
import tv.huan.cms.service.AuthorizationService;
import tv.huan.cms.shiro.PasswordHelper;

import java.util.Calendar;

/**
 * Project Name:BasicCMS
 * File Name:AuthorizationServiceImpl
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:25
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Service
@Slf4j
public class AuthorizationServiceImpl implements AuthorizationService {

    /**
     * 返回消息JSONObject
     */
    private JSONObject serviceMsg;

    @Autowired
    private IUserDAO userDAO;
    @Autowired
    private IRoleDAO roleDAO;
    @Autowired
    private IUserRoleDAO userRoleDAO;
    @Autowired
    private IPermissionDAO permissionDAO;
    @Autowired
    private IRolePermissionDAO rolePermissionDAO;


    /**
     * 构建返回json
     * @param code 返回code
     * @param desc 返回描述
     * @return {"code":code,"description":desc}
     */
    private JSONObject createReturnMsg(int code, String desc){
        serviceMsg = new JSONObject();
        serviceMsg.put("code",code);
        serviceMsg.put("description",desc);
        return serviceMsg;
    }

    /**
     * 构建返回List数据json
     * @param totalCount list总长度
     * @param list 返回分页list
     * @return {"iTotalDisplayRecords":totalCount,"iTotalRecords":totalCount,"aaData":[list]}
     */
    private JSONObject createReturnList(int page, int pageSize, int totalCount, JSONArray list){
        serviceMsg = new JSONObject();
        serviceMsg.put("page", page);
        serviceMsg.put("records", totalCount);
        if(pageSize == 0){
            serviceMsg.put("total",0);
        }else{
            serviceMsg.put("total",totalCount%pageSize == 0 ? totalCount/pageSize : (totalCount/pageSize + 1));
        }
        serviceMsg.put("rows", list);
        log.debug("获取模块列表结果:"+serviceMsg.toJSONString());
        return serviceMsg;
    }

    @Override
    public JSONObject getList(int page, int pageSize, String sidx, String sort, JSONObject searchObj, Class c) {
        JSONArray list;
        int total = 0;
        int offset = (page-1)*pageSize;
        String typeName = c.getSimpleName();
        try {
            switch (typeName){
                case "User" : {
                    list = userDAO.selectUser(offset,pageSize,sidx,sort);
                    total = userDAO.selectUserCount();
                }break;
                case "Role" : {
                    list = roleDAO.selectRole(offset,pageSize,sidx,sort);
                    total = roleDAO.selectRoleCount();
                }break;
                case "Permission" : {
                    list = permissionDAO.selectPermission(offset,pageSize,sidx,sort);
                    total = permissionDAO.selectPermissionCount();
                }break;
                case "UserRole" : {
                    list = userRoleDAO.selectUserRoleList(offset,pageSize,sidx,sort);
                    total = userRoleDAO.selectUserRoleCount();
                }break;
                case "RolePermission" : {
                    list = rolePermissionDAO.selectRolePermission(offset,pageSize,sidx,sort);
                    total = rolePermissionDAO.selectTotalCount();
                }break;
                default:{
                    list = new JSONArray();
                }
            }
            return createReturnList(page,pageSize,total,list);
        }catch (Exception e){
            log.error("获取"+typeName+"列表出错",e);
            return new JSONObject();
        }
    }

    @Override
    public JSONObject save(Object o) {
        String typeName = o.getClass().getSimpleName();
        try {
            switch (typeName){
                case "User" : {
                    User user = (User) o;
                    PasswordHelper ph = new PasswordHelper();
                    ph.encryptPassword(user);
                    user.setCreateDate(Calendar.getInstance().getTime());
                    int insertId = userDAO.insertSelective(user);
                    if(insertId != -1){
                        serviceMsg =  createReturnMsg(1,"add user success");
                        serviceMsg.put("id",insertId);
                    }else{
                        serviceMsg =  createReturnMsg(90202,"add user failed");
                    }
                }break;
                case "Role" : {
                    Role role = (Role) o;
                    Role existRole = roleDAO.selectRoleByRoleName(role.getName());
                    if(existRole != null){
                        serviceMsg = createReturnMsg(90303,"role name already exist,please try anther");
                    }else{
                        int addRes = roleDAO.insertSelective(role);
                        if(addRes == 1){
                            serviceMsg = createReturnMsg(1,"add role success");
                            serviceMsg.put("id",role.getId());
                        }else{
                            serviceMsg = createReturnMsg(90301,"add role failed");
                        }
                    }
                }break;
                case "Permission" : {
                    Permission permission = (Permission)o;
                    Permission existPermission = permissionDAO.selectByName(permission.getName());
                    if(existPermission != null){
                        return createReturnMsg(90314,"add permission name already exist,please try another");
                    }else{
                        int addResult = permissionDAO.insertPermission(permission);
                        if(addResult != -1){
                            serviceMsg = createReturnMsg(1,"add permission success");
                            serviceMsg.put("keyId",addResult);
                        }else{
                            serviceMsg = createReturnMsg(90308,"add permission failed");
                        }
                    }
                }break;
                case "UserRole" : {
                    UserRole ur = (UserRole)o;
                    int res = userRoleDAO.insertSelective(ur.getUser().getId().toString(), ur.getRole().getId().toString());
                    if(res != 0){
                        serviceMsg = createReturnMsg(1,"user bind role success");
                        serviceMsg.put("keyId",res);
                    }else{
                        serviceMsg = createReturnMsg(90315,"user bind role failed");
                    }
                }break;
                case "RolePermission" : {
                    RolePermission rolePermission = (RolePermission)o;
                    int res = rolePermissionDAO.insert(rolePermission);
                    if(res != 0){
                        serviceMsg = createReturnMsg(1,"role bind permission success");
                        serviceMsg.put("keyId",res);
                    }else{
                        serviceMsg = createReturnMsg(90321,"role bind permission failed");
                    }
                }break;
                default:
            }

        }catch (Exception e){
            log.error("添加"+typeName+"出错",e);
            switch (typeName){
                case "User" : {
                    serviceMsg =  createReturnMsg(90203,"add user error");
                }break;
                case "Role" : {
                    serviceMsg = createReturnMsg(90302,"add role error");
                }break;
                case "Permission" : {
                    serviceMsg = createReturnMsg(90309,"add permission error");
                }break;
                case "UserRole" : {
                    serviceMsg = createReturnMsg(90316,"user bind role error");
                }break;
                case "RolePermission" : {
                    serviceMsg = createReturnMsg(90322,"role bind permission error");
                }break;
                default:
            }

        }
        return serviceMsg;

    }

    @Override
    public JSONObject update(Object o) {
        String typeName = o.getClass().getSimpleName();
        try {
            switch (typeName){
                case "User" : {
                    User user = (User) o;
                    int updateRes = userDAO.updateByPrimaryKeySelective(user);
                    if(updateRes == 1){
                        serviceMsg =  createReturnMsg(1,"update user success");
                    }else{
                        serviceMsg =  createReturnMsg(90204,"update user failed");
                    }
                }break;
                case "Role" : {
                    Role role = (Role)o;
                    Role existRole = roleDAO.selectRoleByRoleName(role.getName());
                    if(existRole != null && (!existRole.getId().equals(role.getId()))){
                        serviceMsg = createReturnMsg(90303,"role name already exist,please try anther");
                    }else{
                        int updateRes = roleDAO.updateByPrimaryKeySelective(role);
                        if(updateRes == 1){
                            serviceMsg = createReturnMsg(1,"update role success");
                        }else{
                            serviceMsg = createReturnMsg(90306,"update role failed");
                        }
                    }
                }break;
                case "Permission" : {
                    Permission permission = (Permission)o;
                    Permission existPermission = permissionDAO.selectByName(permission.getName());
                    if(existPermission != null){
                        serviceMsg = createReturnMsg(90314,"update permission name already exist,please try another");
                    }else{
                        int updateResult = permissionDAO.updateByPrimaryKeySelective(permission);
                        if(updateResult == 1){
                            serviceMsg = createReturnMsg(1,"update permission success");
                        }else{
                            serviceMsg = createReturnMsg(90312,"update permission failed");
                        }
                    }
                }break;
                case "UserRole" : {
                    UserRole userRole = (UserRole)o;
                    int updateResult = userRoleDAO.updateByPrimaryKeySelective(userRole);
                    if(updateResult == 1){
                        serviceMsg = createReturnMsg(1,"update userRole success");
                    }else{
                        serviceMsg = createReturnMsg(90317,"update userRole failed");
                    }
                }break;
                case "RolePermission" : {
                    RolePermission rolePermission = (RolePermission)o;
                    int updateResult = rolePermissionDAO.updateByPrimaryKeySelective(rolePermission);
                    if(updateResult == 1){
                        serviceMsg = createReturnMsg(1,"update rolePermission success");
                    }else{
                        serviceMsg = createReturnMsg(90323,"update rolePermission failed");
                    }
                }
                default:
            }
        }catch (Exception e){
            log.error("更新"+typeName+"出错",e);
            switch (typeName){
                case "User" : {
                    serviceMsg =  createReturnMsg(90205,"update user error");
                }break;
                case "Role" : {
                    serviceMsg = createReturnMsg(90307,"update role error");
                }break;
                case "Permission" : {
                    serviceMsg = createReturnMsg(90313,"update permission error");
                }break;
                case "UserRole" : {
                    serviceMsg = createReturnMsg(90318,"update userRole error");
                }break;
                case "RolePermission" : {
                    serviceMsg = createReturnMsg(9032,"update rolePermission error");
                }break;
                default:{
                    serviceMsg = createReturnMsg(0,"null");
                }
            }
        }
        return serviceMsg;
    }

    @Override
    public JSONObject delete(String[] ids, Class c) {
        String typeName = c.getSimpleName();
        try {
            switch (typeName){
                case "User" : {
                    int deleteRes = userDAO.deleteByIdList(ids);
                    if(deleteRes != 0){
                        serviceMsg = createReturnMsg(1,"delete user success");
                    }else{
                        serviceMsg = createReturnMsg(90206,"no user is delete");
                    }
                }break;
                case "Role" : {
                    int deleteRes = roleDAO.deleteByIdArray(ids);
                    if(deleteRes != 0){
                        serviceMsg = createReturnMsg(1,"delete role success");
                    }else{
                        serviceMsg = createReturnMsg(90304,"delete role failed");
                    }
                }break;
                case "Permission" : {
                    int deleteResult = permissionDAO.deleteByIds(ids);
                    if(deleteResult != 0){
                        serviceMsg = createReturnMsg(1,"delete permission success");
                        serviceMsg.put("delete_count",deleteResult);
                    }else{
                        serviceMsg = createReturnMsg(90310,"delete permission failed");
                    }
                }break;
                case "UserRole" : {
                    int deleteResult = userRoleDAO.deleteByIds(ids);
                    if(deleteResult != 0){
                        serviceMsg = createReturnMsg(1,"delete userRole success");
                        serviceMsg.put("delete_count",deleteResult);
                    }else{
                        serviceMsg = createReturnMsg(90319,"delete userRole failed");
                    }
                }break;
                case "RolePermission" : {
                    int deleteResult = rolePermissionDAO.deleteByIds(ids);
                    if(deleteResult != 0){
                        serviceMsg = createReturnMsg(1,"delete rolePermission success");
                        serviceMsg.put("delete_count",deleteResult);
                    }else{
                        serviceMsg = createReturnMsg(90325,"delete rolePermission failed");
                    }
                }
                default:
            }
        }catch (Exception e){
            switch (typeName){
                case "User" : {
                    serviceMsg = createReturnMsg(90207,"delete user error");
                }break;
                case "Role" : {
                    serviceMsg = createReturnMsg(90305,"delete role error");
                }break;
                case "Permission" : {
                    serviceMsg = createReturnMsg(90311,"delete permission error");
                }break;
                case "UserRole" : {
                    serviceMsg = createReturnMsg(90320,"delete userRole error");
                }break;
                case "RolePermission" : {
                    serviceMsg = createReturnMsg(90326,"delete rolePermission error");
                }
                default:
            }
        }
        return serviceMsg;
    }

    @Override
    public JSONObject selectByName(String name, Class c) {
        String typeName = c.getSimpleName();
        try {
            switch (typeName){
                case "User" : {
                    User user = userDAO.selectUserByUserName(name);
                    serviceMsg = (JSONObject) JSON.toJSON(user);
                }break;
                case "Role" : {
                    Role role = roleDAO.selectRoleByRoleName(name);
                    serviceMsg = (JSONObject) JSON.toJSON(role);
                }break;
                case "Permission" : {
                    Permission permission = permissionDAO.selectByName(name);
                    serviceMsg = (JSONObject) JSON.toJSON(permission);
                }break;
                default:{
                    serviceMsg = null;
                }
            }
        }catch (Exception e){
            log.error("根据name:"+typeName+"查询对象出错");
            serviceMsg = null;
        }
        return serviceMsg;
    }

    @Override
    public JSONObject resetUserPwd(String id, String passwd) {
        try {
            User user = new User();
            user.setId(Integer.parseInt(id));
            user.setPassword(passwd);
            PasswordHelper passwordHelper = new PasswordHelper();
            passwordHelper.encryptPassword(user);
            int res = userDAO.updateUserPwd(user);
            if(res == 1){
                serviceMsg = createReturnMsg(1,"reset user password success");
            }else{
                serviceMsg = createReturnMsg(90208,"reset user password failed");
            }
        }catch (Exception e){
            log.info("更新用户密码出错",e);
            serviceMsg = createReturnMsg(90209,"reset user password failed");
        }
        return serviceMsg;
    }

    @Override
    public boolean checkUserName(String username) {
        try {
            User user = userDAO.selectUserByUserName(username);
            return user != null;
        }catch (Exception e){
            log.error("根据username判断用户是否存在出错",e);
            return false;
        }

    }

    @Override
    public JSONObject batchInsertRolePermission(String roleId, String[] pids) {
        try {
            int res = rolePermissionDAO.batchInsert(roleId,pids);
            if(res > 0){
                serviceMsg = createReturnMsg(1,"batch bind role permission success");
            }else{
                serviceMsg = createReturnMsg(90327,"batch bind role permission success");
            }
        }catch (Exception e){
            log.error("批量绑定角色权限出错",e);
            serviceMsg = createReturnMsg(90328,"batch bind role permission success");
        }
        return serviceMsg;
    }


}
