package tv.huan.cms.dao;

import com.alibaba.fastjson.JSONArray;
import tv.huan.cms.entity.Role;

import java.util.List;

/**
 * Project Name:BasicCMS
 * File Name:IRoleDAO
 * Created by wangyuxi on 2018/6/6 下午5:41.
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
public interface IRoleDAO {

    /**
     * 排序获取角色分页数据
     * @param offset 起始位置
     * @param pageSize 偏移量
     * @param orderColumn 排序字段
     * @param orderSort 排序方式
     * @return Role列表
     */
    JSONArray selectRole(int offset, int pageSize, String orderColumn, String orderSort);
    /**
     * 获取角色列表长度
     * @return Role总长度
     */
    int selectRoleCount();

    /**
     * 根据roleName查询role对象
     * @param roleName 角色名
     * @return role对象
     */
    Role selectRoleByRoleName(String roleName);

    /**
     * 根据角色id数组删除对应角色对象
     * @param ids 角色id数组
     * @return 删除数量
     */
    int deleteByIdArray(String[] ids);

    /**
     * 添加角色
     * @param role 角色对象
     * @return 新添加角色主键id
     */
    int insertSelective(Role role);

    /**
     * 更新role对象
     * @param role 待更新对象
     * @return 更新结果
     */
    int updateByPrimaryKeySelective(Role role);






    Role selectByPrimaryKey(Integer id);


    /**
     * 根据username查询用户角色
     * @param userName
     * @return
     */
    List<Role> selectRoleByUserName(String userName);





}
