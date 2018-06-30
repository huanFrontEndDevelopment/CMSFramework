package tv.huan.cms.dao;

import com.alibaba.fastjson.JSONArray;
import tv.huan.cms.entity.UserRole;

/**
 * Project Name:BasicCMS
 * File Name:IUserRoleDAO
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:46
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
public interface IUserRoleDAO {
    /**
     * 分页查询用户角色
     * @param offset 起始位置
     * @param pageSize 分页大小
     * @param orderColumn 排序属性
     * @param orderSort 排序顺序
     * @return UserRole列表
     */
    JSONArray selectUserRoleList(int offset, int pageSize, String orderColumn, String orderSort);

    /**
     * 获取用户角色关系表总长度
     * @return 总长度
     */
    int selectUserRoleCount();

    /**
     * 添加用户角色关系
     * @param userId 用户id
     * @param roleId 角色id
     * @return 新增关系keyid
     */
    int insertSelective(String userId, String roleId);

    /**
     * 单个或批量删除用户角色绑定关系
     * @param ids 待删除绑定关系主键数组
     * @return 删除数量
     */
    int deleteByIds(String[] ids);

    /**
     * 更新用户角色绑定关系
     * @param userRole 角色关系对象
     * @return 1:success
     */
    int updateByPrimaryKeySelective(UserRole userRole);








}
