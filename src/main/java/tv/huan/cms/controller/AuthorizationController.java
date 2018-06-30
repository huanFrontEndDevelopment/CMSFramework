package tv.huan.cms.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tv.huan.cms.entity.*;
import tv.huan.cms.service.AuthorizationService;

/**
 * 权限管理
 * Project Name:BasicCMS
 * File Name:SystemConfigController
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:22
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Slf4j
@Controller
@RequestMapping("/auth")
public class AuthorizationController {


    @Autowired
    private AuthorizationService authorizationService;

    /**
     *  .----------------.  .----------------.  .----------------.  .----------------.
     | .--------------. || .--------------. || .--------------. || .--------------. |
     | | _____  _____ | || |    _______   | || |  _________   | || |  _______     | |
     | ||_   _||_   _|| || |   /  ___  |  | || | |_   ___  |  | || | |_   __ \    | |
     | |  | |    | |  | || |  |  (__ \_|  | || |   | |_  \_|  | || |   | |__) |   | |
     | |  | '    ' |  | || |   '.___`-.   | || |   |  _|  _   | || |   |  __ /    | |
     | |   \ `--' /   | || |  |`\____) |  | || |  _| |___/ |  | || |  _| |  \ \_  | |
     | |    `.__.'    | || |  |_______.'  | || | |_________|  | || | |____| |___| | |
     | |              | || |              | || |              | || |              | |
     | '--------------' || '--------------' || '--------------' || '--------------' |
     '----------------'  '----------------'  '----------------'  '----------------'
     */

    /**
     * 用户列表
     */
    @RequiresPermissions("auth:userList:view")
    @RequestMapping("/userList.do")
    public String goUserManager(){
        return "userList";
    }

    /**
     * 用户列表数据
     * @param page 页码
     * @param rows 分页大小
     * @param sidx 排序索引
     * @param sort 排序方式
     */
    @RequiresPermissions("auth:userList:view")
    @RequestMapping("/userList.json")
    @ResponseBody
    public JSONObject getUserList(@RequestParam(name = "page",defaultValue = "1",required = false) int page,
                                  @RequestParam(name = "rows",defaultValue = "10",required = false) int rows,
                                  @RequestParam(name = "sidx",defaultValue = "id",required = false) String sidx,
                                  @RequestParam(name = "sort",defaultValue = "DESC",required = false) String sort){
        return authorizationService.getList(page,rows,sidx,sort,null,User.class);
    }


    /**
     * 操作用户列表数据
     */
    @RequestMapping("/user/edit.json")
    @ResponseBody
    public JSONObject editUser(@RequestParam(name = "oper") String oper,
                               @RequestParam(name = "id",required = false) String id,
                               @RequestParam(name = "userName",required = false) String userName,
                               @RequestParam(name = "trueName",required = false) String trueName,
                               @RequestParam(name = "password",required = false) String password,
                               @RequestParam(name = "isDelete",required = false) Integer isDelete){
        Subject subject = SecurityUtils.getSubject();
        subject.checkPermission("auth:uerList:"+oper);
        String defaultId = "_empty";
        JSONObject result = new JSONObject();
        switch (oper){
            case "add": {
                boolean userExist = authorizationService.checkUserName(userName);
                if(!userExist){
                    User user = new User();
                    if(trueName != null){
                        user.setTrueName(trueName);
                    }
                    user.setUserName(userName);
                    user.setPassword(password);
                    user.setIsDelete(isDelete);
                    result = authorizationService.save(user);
                }else{
                    result.put("code",90201);
                    result.put("description","username already exist");
                }
            }break;
            case "del":{
                if(!defaultId.equals(id)){
                    String[] ids = checkDelId(id);
                    result = authorizationService.delete(ids,User.class);
                }
            }break;
            case "edit":{
                if(!defaultId.equals(id)) {
                    JSONObject existUser = authorizationService.selectByName(userName,User.class);
                    //更新用户名不可重名
                    if(existUser != null && (!existUser.getString("id").equals(id))){
                        result.put("code",90201);
                        result.put("description","username already exist");
                    }else{
                        User user = new User();
                        if(trueName != null){
                            user.setTrueName(trueName);
                        }
                        int updateUserId = Integer.parseInt(id);
                        user.setUserName(userName);
                        user.setId(updateUserId);
                        user.setIsDelete(isDelete);
                        result = authorizationService.update(user);
                    }

                }
            }break;
            default:{

            }
        }
        return result;
    }

    /**
     * 修改用户密码
     * 需要角色为developer或者administrator才可调用
     * @param id 用户id
     * @param password 新密码
     */
    @RequiresRoles(value={"developer","administrator"}, logical= Logical.OR)
    @RequestMapping("/user/resetPwd.json")
    @ResponseBody
    public JSONObject resetPassword(@RequestParam(name = "id",required = false) String id,
                                    @RequestParam(name = "new_password",required = false) String password){
        return authorizationService.resetUserPwd(id,password);
    }

    /**
     *  .----------------.  .----------------.  .----------------.  .----------------.
     | .--------------. || .--------------. || .--------------. || .--------------. |
     | |  _______     | || |     ____     | || |   _____      | || |  _________   | |
     | | |_   __ \    | || |   .'    `.   | || |  |_   _|     | || | |_   ___  |  | |
     | |   | |__) |   | || |  /  .--.  \  | || |    | |       | || |   | |_  \_|  | |
     | |   |  __ /    | || |  | |    | |  | || |    | |   _   | || |   |  _|  _   | |
     | |  _| |  \ \_  | || |  \  `--'  /  | || |   _| |__/ |  | || |  _| |___/ |  | |
     | | |____| |___| | || |   `.____.'   | || |  |________|  | || | |_________|  | |
     | |              | || |              | || |              | || |              | |
     | '--------------' || '--------------' || '--------------' || '--------------' |
     '----------------'  '----------------'  '----------------'  '----------------'
     */

    /**
     * 角色列表
     */
    @RequiresPermissions("auth:roleList:view")
    @RequestMapping("/roleList.do")
    public String goRoleList(){
        return "roleList";
    }


    /**
     * 分页获取角色列表
     * @param page 页码
     * @param rows 分页大小
     * @param sidx 排序属性
     * @param sort 排序方向
     */
    @RequiresPermissions("auth:roleList:view")
    @RequestMapping("/roleList.json")
    @ResponseBody
    public JSONObject getRoleList(@RequestParam(name = "page",defaultValue = "1",required = false) int page,
                                  @RequestParam(name = "rows",defaultValue = "10",required = false) int rows,
                                  @RequestParam(name = "sidx",defaultValue = "id",required = false) String sidx,
                                  @RequestParam(name = "sort",defaultValue = "DESC",required = false) String sort){
        return authorizationService.getList(page,rows,sidx,sort,null,Role.class);
    }

    /**
     * 角色编辑
     * @param oper 编辑操作符
     * @param id 角色id
     * @param name 角色名
     * @param alias 角色别名
     * @return JSON
     */
    @RequestMapping("/role/edit.json")
    @ResponseBody
    public JSONObject editRole(@RequestParam(name = "oper") String oper,
                               @RequestParam(name = "id",required = false) String id,
                               @RequestParam(name = "name",required = false)String name,
                               @RequestParam(name = "alias",required = false)String alias){
        String defaultId = "_empty";
        JSONObject result = new JSONObject();
        switch (oper){
            case "add": {
                JSONObject existRole = authorizationService.selectByName(name,Role.class);
                if(existRole != null){
                    result.put("code",90329);
                    result.put("description","rolename already exist");
                }else{
                    Role role = new Role();
                    role.setName(name);
                    if(alias != null){
                        role.setAlias(alias);
                    }
                    result = authorizationService.save(role);
                }

            }break;
            case "del":{

                if(!defaultId.equals(id)){
                    String[] ids = checkDelId(id);
                    result = authorizationService.delete(ids,Role.class);
                }
            }break;
            case "edit":{
                if(!defaultId.equals(id)) {
                    JSONObject existRole = authorizationService.selectByName(name,Role.class);
                    if(existRole != null && (!existRole.getString("id").equals(id))){
                        result.put("code",90329);
                        result.put("description","rolename already exist");
                    }else{
                        Role role = new Role();
                        role.setId(Integer.parseInt(id));
                        if(alias != null){
                            role.setAlias(alias);
                        }
                        role.setName(name);
                        result = authorizationService.update(role);
                    }

                }
            }break;
            default:{

            }
        }
        return result;
    }

    /**
     *  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .-----------------.
     | .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |
     | |   ______     | || |  _________   | || |  _______     | || | ____    ____ | || |     _____    | || |    _______   | || |    _______   | || |     _____    | || |     ____     | || | ____  _____  | |
     | |  |_   __ \   | || | |_   ___  |  | || | |_   __ \    | || ||_   \  /   _|| || |    |_   _|   | || |   /  ___  |  | || |   /  ___  |  | || |    |_   _|   | || |   .'    `.   | || ||_   \|_   _| | |
     | |    | |__) |  | || |   | |_  \_|  | || |   | |__) |   | || |  |   \/   |  | || |      | |     | || |  |  (__ \_|  | || |  |  (__ \_|  | || |      | |     | || |  /  .--.  \  | || |  |   \ | |   | |
     | |    |  ___/   | || |   |  _|  _   | || |   |  __ /    | || |  | |\  /| |  | || |      | |     | || |   '.___`-.   | || |   '.___`-.   | || |      | |     | || |  | |    | |  | || |  | |\ \| |   | |
     | |   _| |_      | || |  _| |___/ |  | || |  _| |  \ \_  | || | _| |_\/_| |_ | || |     _| |_    | || |  |`\____) |  | || |  |`\____) |  | || |     _| |_    | || |  \  `--'  /  | || | _| |_\   |_  | |
     | |  |_____|     | || | |_________|  | || | |____| |___| | || ||_____||_____|| || |    |_____|   | || |  |_______.'  | || |  |_______.'  | || |    |_____|   | || |   `.____.'   | || ||_____|\____| | |
     | |              | || |              | || |              | || |              | || |              | || |              | || |              | || |              | || |              | || |              | |
     | '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |
     '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'
     */

    /**
     *权限列表
     */
    @RequiresPermissions("auth:permissionList:view")
    @RequestMapping("/permissionList.do")
    public String goPermissionList(){
        return "permissionList";
    }

    /**
     * 分页权限管理数据
     * @param page 页码
     * @param rows 分页大小
     * @param sidx 排序字段
     * @param sort 排序顺序
     * @return 分页权限管理json
     */
    @RequestMapping("/permissionList.json")
    @ResponseBody
    public JSONObject getPermissionList(@RequestParam(name = "page",defaultValue = "1",required = false) int page,
                                        @RequestParam(name = "rows",defaultValue = "10",required = false) int rows,
                                        @RequestParam(name = "sidx",defaultValue = "id",required = false) String sidx,
                                        @RequestParam(name = "sort",defaultValue = "DESC",required = false) String sort){

        return authorizationService.getList(page,rows,sidx,sort,null,Permission.class);
    }

    /**
     * 角色编辑
     * @param oper 编辑操作符
     * @param id 权限id
     * @param name 权限名
     * @param url 权限url
     * @param expression 权限表达式
     * @return JSON
     */
    @RequestMapping("/permission/edit.json")
    @ResponseBody
    public JSONObject editPermission(@RequestParam(name = "oper") String oper,
                                     @RequestParam(name = "id",required = false) String id,
                                     @RequestParam(name = "name",required = false)String name,
                                     @RequestParam(name = "url",required = false)String url,
                                     @RequestParam(name = "expression",required = false)String expression){
        Subject subject = SecurityUtils.getSubject();
        subject.checkPermission("auth:PermissionList:"+oper);
        String defaultId = "_empty";
        JSONObject result = new JSONObject();
        switch (oper){
            case "add": {
                JSONObject existPerm = authorizationService.selectByName(name,Permission.class);
                if(existPerm != null){
                    result.put("code",90330);
                    result.put("description","permission name already exist");
                }else{
                    Permission permission = new Permission(null,name,url,expression,null);
                    result = authorizationService.save(permission);
                }
            }break;
            case "del":{
                if(!defaultId.equals(id)){
                    String[] ids = checkDelId(id);
                    result = authorizationService.delete(ids,Permission.class);
                }
            }break;
            case "edit":{
                if(!defaultId.equals(id)) {
                    JSONObject existPerm = authorizationService.selectByName(name,Permission.class);
                    if(existPerm != null && (!existPerm.getString("id").equals(id))){
                        result.put("code",90330);
                        result.put("description","permission name already exist,please try another");
                    }else{
                        Permission permission = new Permission(Integer.parseInt(id),name,url,expression,null);
                        result = authorizationService.update(permission);
                    }
                }
            }break;
            default:{
                result.put("code",2);
                result.put("description","未定义的oper");
            }
        }
        return result;
    }

    /**
     *  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.
     | .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |
     | | _____  _____ | || |    _______   | || |  _________   | || |  _______     | || |  _______     | || |     ____     | || |   _____      | || |  _________   | |
     | ||_   _||_   _|| || |   /  ___  |  | || | |_   ___  |  | || | |_   __ \    | || | |_   __ \    | || |   .'    `.   | || |  |_   _|     | || | |_   ___  |  | |
     | |  | |    | |  | || |  |  (__ \_|  | || |   | |_  \_|  | || |   | |__) |   | || |   | |__) |   | || |  /  .--.  \  | || |    | |       | || |   | |_  \_|  | |
     | |  | '    ' |  | || |   '.___`-.   | || |   |  _|  _   | || |   |  __ /    | || |   |  __ /    | || |  | |    | |  | || |    | |   _   | || |   |  _|  _   | |
     | |   \ `--' /   | || |  |`\____) |  | || |  _| |___/ |  | || |  _| |  \ \_  | || |  _| |  \ \_  | || |  \  `--'  /  | || |   _| |__/ |  | || |  _| |___/ |  | |
     | |    `.__.'    | || |  |_______.'  | || | |_________|  | || | |____| |___| | || | |____| |___| | || |   `.____.'   | || |  |________|  | || | |_________|  | |
     | |              | || |              | || |              | || |              | || |              | || |              | || |              | || |              | |
     | '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |
     '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'
     */

    /**
     *用户角色列表
     */
    @RequiresPermissions("auth:userRole:view")
    @RequestMapping("/userBindRole.do")
    public String goUserBindRoleList(ModelMap map){
        JSONObject roleList = authorizationService.getList(0,0,null,null,null,Role.class);
        JSONObject userList = authorizationService.getList(0,0,null,null,null,User.class);
        map.put("userSelectVal",formatSelectValue(userList.getJSONArray("rows"),"userName"));
        map.put("roleSelectVal",formatSelectValue(roleList.getJSONArray("rows"),"name"));
        return "userBindRole";
    }

    /**
     *
     * 分页用户角色数据
     * @param page 页码
     * @param rows 分页大小
     * @param sidx 排序字段
     * @param sort 排序方式
     * @return 用户角色分页数据
     */
    @RequiresPermissions("auth:userRole:view")
    @RequestMapping("/userRoleList.json")
    @ResponseBody
    public JSONObject getUserBindRoleList(@RequestParam(name = "page",defaultValue = "1",required = false) int page,
                                          @RequestParam(name = "rows",defaultValue = "10",required = false) int rows,
                                          @RequestParam(name = "sidx",defaultValue = "id",required = false) String sidx,
                                          @RequestParam(name = "sort",defaultValue = "DESC",required = false) String sort){
        return authorizationService.getList(page,rows,sidx,sort,null, UserRole.class);
    }
    /**
     * 用户角色编辑
     * @param oper 编辑操作符
     * @param id 用户角色关系id
     * @param userId 用户id
     * @param roleId 角色id
     * @return JSON
     */

    @RequestMapping("/userRole/edit.json")
    @ResponseBody
    public JSONObject editUserRole(@RequestParam(name = "oper") String oper,
                                   @RequestParam(name = "id",required = false) String id,
                                   @RequestParam(name = "userId",required = false)String userId,
                                   @RequestParam(name = "roleId",required = false)String roleId){
        Subject subject = SecurityUtils.getSubject();
        subject.checkPermission("auth:userRole:"+oper);
        String defaultId = "_empty";
        JSONObject result = new JSONObject();
        switch (oper){
            case "add": {
                UserRole userRole = new UserRole();
                User user = new User();
                user.setId(Integer.parseInt(userId));
                Role role = new Role();
                role.setId(Integer.parseInt(roleId));
                userRole.setRole(role);
                userRole.setUser(user);
                result = authorizationService.save(userRole);
            }break;
            case "del":{
                if(!defaultId.equals(id)){
                    String[] ids = checkDelId(id);
                    result = authorizationService.delete(ids,UserRole.class);
                }
            }break;
            case "edit":{
                UserRole userRole = new UserRole();
                userRole.setId(Integer.parseInt(id));
                User user = new User();
                user.setId(Integer.parseInt(userId));
                Role role = new Role();
                role.setId(Integer.parseInt(roleId));
                userRole.setRole(role);
                userRole.setUser(user);
                result = authorizationService.update(userRole);
            }break;
            default:{
                result.put("code",2);
                result.put("description","未定义的oper");
            }
        }
        return result;
    }

    /**
     *  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .-----------------.
     | .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |
     | |  _______     | || |     ____     | || |   _____      | || |  _________   | || |   ______     | || |  _________   | || |  _______     | || | ____    ____ | || |     _____    | || |    _______   | || |    _______   | || |     _____    | || |     ____     | || | ____  _____  | |
     | | |_   __ \    | || |   .'    `.   | || |  |_   _|     | || | |_   ___  |  | || |  |_   __ \   | || | |_   ___  |  | || | |_   __ \    | || ||_   \  /   _|| || |    |_   _|   | || |   /  ___  |  | || |   /  ___  |  | || |    |_   _|   | || |   .'    `.   | || ||_   \|_   _| | |
     | |   | |__) |   | || |  /  .--.  \  | || |    | |       | || |   | |_  \_|  | || |    | |__) |  | || |   | |_  \_|  | || |   | |__) |   | || |  |   \/   |  | || |      | |     | || |  |  (__ \_|  | || |  |  (__ \_|  | || |      | |     | || |  /  .--.  \  | || |  |   \ | |   | |
     | |   |  __ /    | || |  | |    | |  | || |    | |   _   | || |   |  _|  _   | || |    |  ___/   | || |   |  _|  _   | || |   |  __ /    | || |  | |\  /| |  | || |      | |     | || |   '.___`-.   | || |   '.___`-.   | || |      | |     | || |  | |    | |  | || |  | |\ \| |   | |
     | |  _| |  \ \_  | || |  \  `--'  /  | || |   _| |__/ |  | || |  _| |___/ |  | || |   _| |_      | || |  _| |___/ |  | || |  _| |  \ \_  | || | _| |_\/_| |_ | || |     _| |_    | || |  |`\____) |  | || |  |`\____) |  | || |     _| |_    | || |  \  `--'  /  | || | _| |_\   |_  | |
     | | |____| |___| | || |   `.____.'   | || |  |________|  | || | |_________|  | || |  |_____|     | || | |_________|  | || | |____| |___| | || ||_____||_____|| || |    |_____|   | || |  |_______.'  | || |  |_______.'  | || |    |_____|   | || |   `.____.'   | || ||_____|\____| | |
     | |              | || |              | || |              | || |              | || |              | || |              | || |              | || |              | || |              | || |              | || |              | || |              | || |              | || |              | |
     | '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |
     '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'
     */

    /**
     *角色权限列表
     */
    @RequiresPermissions("auth:rolePermission:view")
    @RequestMapping("/RolePermissionList.do")
    public String goRolePermissionList(ModelMap map){
        JSONObject permissionList = authorizationService.getList(0,0,null,null,null,Permission.class);
        map.put("permissionSelectVal",formatSelectValue(permissionList.getJSONArray("rows"),"name"));
        JSONObject roleList = authorizationService.getList(0,0,null,null,null,Role.class);
        map.put("roleSelectVal",formatSelectValue(roleList.getJSONArray("rows"),"name"));
        return "rolePermissionList";
    }

    /**
     * 分页角色权限数据
     * @param page 页码
     * @param rows 分页大小
     * @param sidx 排序字段
     * @param sort 排序方式
     * @return 角色权限分页数据
     */
    @RequiresPermissions("auth:rolePermission:view")
    @RequestMapping("/RolePermissionList.json")
    @ResponseBody
    public JSONObject getRolePermissionList(@RequestParam(name = "page",defaultValue = "1",required = false) int page,
                                            @RequestParam(name = "rows",defaultValue = "10",required = false) int rows,
                                            @RequestParam(name = "sidx",defaultValue = "id",required = false) String sidx,
                                            @RequestParam(name = "sort",defaultValue = "DESC",required = false) String sort){
        return authorizationService.getList(page,rows,sidx,sort,null, RolePermission.class);
    }
    /**
     * 角色权限编辑
     * @param oper 编辑操作符
     * @param id 用户角色关系id
     * @param permissionId 权限id
     * @param roleId 角色id
     * @return JSON
     */
    @RequestMapping("/rolePermission/edit.json")
    @ResponseBody
    public JSONObject editRolePermission(@RequestParam(name = "oper") String oper,
                                         @RequestParam(name = "id",required = false) String id,
                                         @RequestParam(name = "permissionId",required = false)String permissionId,
                                         @RequestParam(name = "roleId",required = false)String roleId){
        Subject subject = SecurityUtils.getSubject();
        subject.checkPermission("auth:rolePermission:"+oper);
        String defaultId = "_empty";
        String batchInsertFlag = ",";
        JSONObject result = new JSONObject();
        switch (oper){
            case "add": {
                if(permissionId.contains(batchInsertFlag)){
                    String[] pids = checkDelId(permissionId);
                    result = authorizationService.batchInsertRolePermission(roleId,pids);
                }else{
                    RolePermission rolePermission = new RolePermission();
                    Role role = new Role(Integer.parseInt(roleId),null,null);
                    Permission permission = new Permission(Integer.parseInt(permissionId),null,null,null,null);
                    rolePermission.setRole(role);
                    rolePermission.setPermission(permission);
                    result = authorizationService.save(rolePermission);
                }
            }break;
            case "del":{
                if(!defaultId.equals(id)){
                    String[] ids = checkDelId(id);
                    result = authorizationService.delete(ids,RolePermission.class);
                }
            }break;
            case "edit":{
                RolePermission rolePermission = new RolePermission();
                Role role = new Role();
                if(roleId != null){
                    role.setId(Integer.parseInt(roleId));
                }
                Permission permission = new Permission();
                if(permissionId != null){
                    permission.setId(Integer.parseInt(permissionId));
                }
                rolePermission.setId(Integer.parseInt(id));
                rolePermission.setPermission(permission);
                rolePermission.setRole(role);
                result = authorizationService.update(rolePermission);
            }break;
            default:{
                result.put("code",2);
                result.put("description","未定义的oper");
            }
        }
        return result;
    }

    private String formatSelectValue(JSONArray array, String keyName){
        StringBuilder sb = new StringBuilder();
        for(int i=0 ; i<array.size() ; i++){
            JSONObject temp = array.getJSONObject(i);
            sb.append(temp.getIntValue("id")).append(":").append(temp.getString(keyName));
            if(i!=(array.size()-1)){
                sb.append(";");
            }
        }
        return sb.toString();
    }
    private String[] checkDelId(String delId){
        //批量删除检测符
        String multiFlag = ",";
        String[] ids;
        if(delId.contains(multiFlag)){
            ids = delId.split(",");
        }else{
            ids = new String[1];
            ids[0] = delId;
        }
        return ids;
    }


}

