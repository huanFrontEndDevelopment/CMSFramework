package tv.huan.cms.service;

import com.alibaba.fastjson.JSONObject;

/**
 * Project Name:BasicCMS
 * File Name:AuthorizationService
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:23
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
public interface AuthorizationService {
    /**
     * 模块通用获取数据列表
     * @param page 当前页码
     * @param pageSize 分页大小
     * @param sidx 排序列名
     * @param sort 排序方向
     * @param searchObj 搜索条件
     * @param c 返回列表类型
     * @return JsonObject 分页列表
     */
    JSONObject getList(int page, int pageSize, String sidx, String sort, JSONObject searchObj, Class c);

    /**
     * 模块通用保存
     * @param o 待保存对象
     * @return JSONObject {"code":1,"description":"success"}
     */
    JSONObject save(Object o);

    /**
     * 通用更新
     * @param o 待更新对象
     * @return JSONObject {"code":1,"description":"success"}
     */
    JSONObject update(Object o);

    /**
     * 通用删除
     * @param ids 待删除主键id数组
     * @param c 删除对象类型
     * @return JSONObject {"code":1,"description":"success"}
     */
    JSONObject delete(String[] ids, Class c);

    /**
     * 通用根据名称查询
     * @param name 名称
     * @param c 查询类型
     * @return 查询对象json
     */
    JSONObject selectByName(String name, Class c);

    /**
     * 密码重置
     * @param id 用户id
     * @param passwd 新密码
     * @return code   1:成功,93501:失败,93502:出错
     */
    JSONObject resetUserPwd(String id, String passwd);
    /**
     * 查询用户名是否存在
     * @param username 用户名
     * @return true:用户存在 false:用户不存在
     */
    boolean checkUserName(String username);

    /**
     * 批量插入角色权限
     * @param roleId 角色id
     * @param pids 权限id数组
     * @return JSONObject {"code":1,"description":"success"}
     */
    JSONObject batchInsertRolePermission(String roleId, String[] pids);



}
