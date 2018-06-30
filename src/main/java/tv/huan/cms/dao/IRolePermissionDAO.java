package tv.huan.cms.dao;

import com.alibaba.fastjson.JSONArray;
import tv.huan.cms.entity.RolePermission;

/**
 * Project Name:BasicCMS
 * File Name:IRolePermissionDAO
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:55
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
public interface IRolePermissionDAO {
    /**
     * 分页查询角色权限
     * @param offset 起始位置
     * @param pageSize 分页大小
     * @param orderColumn 排序属性
     * @param orderSort 排序顺序
     * @return RolePermission列表
     */
    JSONArray selectRolePermission(int offset, int pageSize, String orderColumn, String orderSort);

    /**
     * 获取所有角色权限关系数量
     * @return 全部关系数量
     */
    int selectTotalCount();

    /**
     * 添加新角色权限绑定
     *
     * @param rolePermission 角色权限对象
     * @return 1:success
     */
    int insert(RolePermission rolePermission);
    /**
     * 更新角色权限绑定关系(可选更新字段)
     *
     * @param rolePermission 角色权限绑定对象
     * @return 1:success
     */
    int updateByPrimaryKeySelective(RolePermission rolePermission);

    /**
     * 根据id数组批量删除绑定关系
     * @param ids keyId数组
     * @return 删除数量
     */
    int deleteByIds(String[] ids);

    /**
     * 批量插入角色权限绑定关系
     * @param roleId 角色id
     * @param permissionIds 权限id数组
     * @return 绑定数量
     */
    int batchInsert(String roleId, String[] permissionIds);


    /**
     * 根据keyId查询绑定关系
     *
     * @param id keyId
     * @return RolePermission object
     */
    RolePermission selectByPrimaryKey(Integer id);


}
