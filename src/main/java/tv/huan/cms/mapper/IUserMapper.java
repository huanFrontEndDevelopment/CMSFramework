package tv.huan.cms.mapper;

import org.apache.ibatis.annotations.*;
import tv.huan.cms.entity.User;
import tv.huan.cms.mapper.sql.UserSqlProvider;

import java.util.List;

/**
 * Project Name:BasicCMS
 * File Name:IUserMapper
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:32
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Mapper
public interface IUserMapper {

    /**
     * 排序获取用户分页数据
     * @param offset 起始位置
     * @param pageSize 偏移量
     * @param orderColumn 排序字段
     * @param orderSort 排序方式
     * @return User列表
     */
    @SelectProvider(type = UserSqlProvider.class , method = "selectUser")
    List<User> selectUser(@Param("offset") int offset, @Param("pageSize") int pageSize, @Param("orderColumn") String orderColumn, @Param("orderSort") String orderSort);

    /**
     * 获取用户总数
     * @return 用户总数量
     */
    @Select("select count(id) from user")
    int selectUserCount();
    /**
     * 根据username获取user对象
     * @param name 用户名
     * @return User对象
     */
    @Select("select * from user where userName = #{name}")
    User selectUserByName(@Param("name") String name);
    /**
     * 根据id查询用户
     * @param id 用户id
     * @return User对象
     */
    @Select("select * from user where id = #{id}")
    User selectByPrimaryKey(Integer id);

    /**
     * 用户UserRole关联查询(只查询用户名和id)
     * @param id 用户id
     * @return User对象
     */
    @Select("select id, userName from user where id = #{id}")
    User joinSelectById(Integer id);
    /**
     * 新增user
     * @param user user对象
     * @return 添加成功:1
     */
    @InsertProvider(type = UserSqlProvider.class,method = "insertUser")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    int insertSelective(@Param("user") User user);
    /**
     * 批量删除
     * @param ids 删除id列表
     * @return 删除总数
     */

    @DeleteProvider(type = UserSqlProvider.class,method = "deleteUserByIds")
    int deleteByIdList(@Param("ids") String[] ids);
    /**
     * 重置用户密码
     * @param user 用户信息bean
     * @return 更新结果res
     */
    @Update("update user set password = #{user.password},credentialsSalt = #{user.credentialsSalt} where id = #{user.id}")
    int updateUserPwd(@Param("user") User user);
    /**
     * 更新用户
     * @param user 用户对象
     * @return 更新行数 1:success
     */
    @UpdateProvider(type = UserSqlProvider.class,method = "updateUser")
    int updateByPrimaryKeySelective(User user);
}
