package tv.huan.cms.mapper;

import org.apache.ibatis.annotations.*;
import tv.huan.cms.entity.Permission;
import tv.huan.cms.mapper.sql.PermissionSqlProvider;

import java.util.List;
import java.util.Map;

/**
 * Project Name:BasicCMS
 * File Name:IPermissionMapper
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:48
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Mapper
public interface IPermissionMapper {

    /**
     * 分页获取权限list
     * @param offset 起始位置
     * @param pageSize 偏移量
     * @param orderColumn 排序字段
     * @param orderSort 排序方式
     * @return permission list
     */
    @SelectProvider(type = PermissionSqlProvider.class, method = "selectPermission")
    List<Permission> selectPermission(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("orderColumn") String orderColumn, @Param("orderSort") String orderSort);

    /**
     * 获取权限列表长度
     * @return permission list total count
     */
    @Select("select count(id) from permission")
    int selectPermissionCount();

    /**
     * 添加权限
     * @param permission 添加权限对象
     * @return 添加结果,1:成功
     */
    @InsertProvider(type = PermissionSqlProvider.class, method = "insertPermission")
    @Options(useGeneratedKeys = true, keyProperty = "permission.id")
    int insertSelective(@Param("permission") Permission permission);

    /**
     * 根据id list 删除一个或多个权限对象
     * @param map 包含String[] ids的HashMap
     * @return 删除权限数量
     */
    @DeleteProvider(type = PermissionSqlProvider.class, method = "deletePermission")
    int deleteByIdList(Map<String, Object> map);

    /**
     * 更新权限
     * @param permission 权限对象
     * @return 1:更新成功
     */
    @UpdateProvider(type = PermissionSqlProvider.class, method = "updatePermission")
    int updateByPrimaryKeySelective(Permission permission);

    /**
     * 根据权限名查询权限
     * @param name 权限名
     * @return name对应权限对象
     */
    @Select("select * from permission where name = #{name}")
    Permission selectByName(String name);


    /**
     * 根据id查询权限
     * @param id 主键id
     * @return Permission对象
     */
    @Select("select * from permission where id=#{id}")
    Permission selectByPrimaryKey(Integer id);


    /**
     * 根据username获取用户权限
     * @param username 用户名
     * @return 权限列表
     */
    @Select("select * from permission as p where p.id in (select rp.permission_id from role_permission as rp where rp.role_id in (select ur.role_id from user_role as ur where ur.user_id = (select u.id from user as u where u.userName = #{username})))")
    List<Permission> selectUserBindPermissionByUserName(String username);

    /**
     * 根据用户角色id获取用户角色所绑定权限的权限url
     * @param roleId 用户角色id
     * @return 权限permission url列表
     */
    @Select("select p.url from permission as p where p.id in(select rp.permission_id from role_permission as rp where rp.role_id = #{roleId}) group by p.url order by null")
    List<String> selectRolePermissionUrlByRoleId(String roleId);






}
