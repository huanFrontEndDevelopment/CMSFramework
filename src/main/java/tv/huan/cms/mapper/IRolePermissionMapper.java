package tv.huan.cms.mapper;

import org.apache.ibatis.annotations.*;
import tv.huan.cms.entity.RolePermission;
import tv.huan.cms.mapper.sql.RolePermissionSqlProvider;

import java.util.List;

/**
 * Project Name:BasicCMS
 * File Name:IRolePermissionMapper
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:54
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Mapper
public interface IRolePermissionMapper {
    /**
     * 分页查询RolePermission对象
     * @param offset 起始位置
     * @param pageSize 偏移量
     * @param orderColumn 排序字段
     * @param orderSort 排序方式
     * @return RolePermission列表
     */
    @SelectProvider(type = RolePermissionSqlProvider.class, method = "selectRolePermission")
    @Results({
            @Result(property = "permission", column = "permission_id",
                    one = @One(select = "tv.huan.cms.mapper.IPermissionMapper.selectByPrimaryKey")),
            @Result(property = "role", column = "role_id",
                    one = @One(select = "tv.huan.cms.mapper.IRoleMapper.joinSelectById"))
    })
    List<RolePermission> selectRolePermission(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("orderColumn") String orderColumn, @Param("orderSort") String orderSort);

    /**
     * 获取所有RolePermission
     * @return RolePermission 总数
     */
    @Select("select count(id) from role_permission")
    int selectTotalCount();

    /**
     * 添加新角色权限绑定
     *
     * @param rolePermission 角色权限对象
     * @return 1:success
     */
    @InsertProvider(type = RolePermissionSqlProvider.class, method = "insertRolePermission")
    @Options(useGeneratedKeys = true, keyProperty = "rolePermission.id")
    int insert(@Param("rolePermission") RolePermission rolePermission);

    /**
     * 批量插入角色权限绑定
     * @param roleId 角色id(single)
     * @param permissionIds 权限id(Multiple)
     * @return 更新数量
     */
    @InsertProvider(type = RolePermissionSqlProvider.class, method = "batchInsert")
    int batchInsert(@Param("roleId") String roleId, @Param("permissionIds") String[] permissionIds);

    /**
     * 根据id数组批量删除对应关系
     * @param ids id数组
     * @return 删除数量
     */
    @DeleteProvider(type = RolePermissionSqlProvider.class, method = "deleteRolePermission")
    int deleteByIds(@Param("ids") String[] ids);
    /**
     * 更新角色权限绑定关系(可选更新字段)
     *
     * @param rolePermission 角色权限绑定对象
     * @return 1:success
     */
    @UpdateProvider(type = RolePermissionSqlProvider.class, method = "upateRolePermission")
    int updateByPrimaryKeySelective(RolePermission rolePermission);
    /**
     * 根据keyId查询绑定关系
     *
     * @param id keyId
     * @return RolePermission object
     */
    @Select("select * from role_permission where id = #{id}")
    RolePermission selectByPrimaryKey(Integer id);

}
