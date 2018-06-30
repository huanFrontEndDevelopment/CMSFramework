package tv.huan.cms.mapper;

import org.apache.ibatis.annotations.*;
import tv.huan.cms.entity.Role;
import tv.huan.cms.mapper.sql.RoleSqlProvider;

import java.util.List;

/**
 * Project Name:BasicCMS
 * File Name:IRoleMapper
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:41
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Mapper
public interface IRoleMapper {


    /**
     * 排序获取角色分页数据
     * @param offset 起始位置
     * @param pageSize 偏移量
     * @param orderColumn 排序字段
     * @param orderSort 排序方式
     * @return Role列表
     */
    @SelectProvider(type = RoleSqlProvider.class,method = "selectRole")
    List<Role> selectRole(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("orderColumn") String orderColumn, @Param("orderSort") String orderSort);
    /**
     * 获取角色列表长度
     * @return Role总长度
     */
    @Select("select count(id) from role")
    int selectRoleCount();

    /**
     * 添加角色
     * @param role 角色对象
     * @return 1:success
     */
    @InsertProvider(type = RoleSqlProvider.class,method = "insertRole")
    @Options(useGeneratedKeys = true, keyProperty = "role.id")
    int insertSelective(@Param("role") Role role);

    /**
     * 根据id数组批量删除角色
     * @param ids 角色id数组
     * @return 角色删除数量
     */
    @DeleteProvider(type = RoleSqlProvider.class,method = "deleteRole")
    int deleteByIdList(@Param("ids") String[] ids);

    /**
     * 更新role对象
     * @param role 待更新对象
     * @return 更新结果
     */
    @UpdateProvider(type = RoleSqlProvider.class,method = "updateRole")
    int updateByPrimaryKeySelective(Role role);

    /**
     * 根据id关联查询role,用户userRole 外键查询
     * @param id role_id
     * @return role对象
     *
     */
    @Select("select id, name from role where id = #{id}")
    Role joinSelectById(Integer id);


    /**
     * 根据id获取Role对象
     * @param id 主键id
     * @return role对象
     */
    @Select("select * from role where id = #{id}")
    Role selectByPrimaryKey(Integer id);





    /**
     * 根据username查询用户角色
     * @param userName 用户名
     * @return role角色列表
     */
    @Select("select * from role where id IN(select role_id from user_role where user_id = (select id from user where userName = #{userName}))")
    List<Role> selectRoleByUserName(String userName);
    /**
     * 根据roleName查询role对象
     * @param roleName 角色名
     * @return role对象
     */
    @Select("select * from role as r where r.name = #{roleName}")
    Role selectRoleByRoleName(String roleName);




}
