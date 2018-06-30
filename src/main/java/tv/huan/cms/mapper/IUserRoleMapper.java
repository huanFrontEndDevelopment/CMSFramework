package tv.huan.cms.mapper;

import org.apache.ibatis.annotations.*;
import tv.huan.cms.entity.UserRole;
import tv.huan.cms.mapper.sql.UserRoleSqlProvider;

import java.util.List;

/**
 * mybatis操作user_role表
 * Project Name:BasicCMS
 * File Name:IUserRoleMapper
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:43
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
//@Repository
@Mapper
public interface IUserRoleMapper {
    /**
     * 分页查询用户角色
     * @param offset 起始位置
     * @param pageSize 分页大小
     * @param orderColumn 排序属性
     * @param orderSort 排序顺序
     * @return UserRole列表
     */
    @SelectProvider(type = UserRoleSqlProvider.class,method = "selectUserRoleList")
    @Results({
            @Result(property = "user", column = "user_id",
                    one = @One(select = "tv.huan.cms.mapper.IUserMapper.joinSelectById")),
            @Result(property = "role", column = "role_id",
                    one = @One(select = "tv.huan.cms.mapper.IRoleMapper.joinSelectById"))
    })
    List<UserRole> selectUserRoleList(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("orderColumn") String orderColumn, @Param("orderSort") String orderSort);
    /**
     * 获取用户角色关系表总长度
     * @return 总长度
     */
    @Select("select count(id) from user_role")
    int selectUserRoleCount();
    /**
     * 插入用户角色绑定关系
     * @param userRole 用户角色关系对象
     * @return 1:success
     */
    @InsertProvider(type = UserRoleSqlProvider.class, method = "insertUserRole")
    @Options(useGeneratedKeys = true, keyProperty = "userRole.id")
    int insertSelective(@Param("userRole") UserRole userRole);

    /**
     * 根据id删除绑定关系
     * @param ids keyId数组
     * @return 删除数量
     */
    @DeleteProvider(type = UserRoleSqlProvider.class,method = "deleteUserRole")
    int deleteByIdList(@Param("ids") String[] ids);

    /**
     * 更新用户角色绑定关系
     * @param id 绑定关系主键id
     * @param userId 用户id
     * @param roleId 角色id
     * @return 1:success
     */
    @UpdateProvider(type = UserRoleSqlProvider.class,method = "updateUserRole")
    int updateUserRole(@Param("id") Integer id, @Param("userId") Integer userId, @Param("roleId") Integer roleId);

}
