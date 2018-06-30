package tv.huan.cms.dao;

import com.alibaba.fastjson.JSONArray;
import tv.huan.cms.entity.User;

/**
 * Project Name:BasicCMS
 * File Name:IUserDAO
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:33
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
public interface IUserDAO {


    /**
     * 排序获取用户分页数据
     * @param offset 起始位置
     * @param pageSize 偏移量
     * @param orderColumn 排序字段
     * @param orderSort 排序方式
     * @return User列表
     */
    JSONArray selectUser(int offset, int pageSize, String orderColumn, String orderSort);

    /**
     * 获取用户总数
     * @return 用户总数
     */
    int selectUserCount();

    /**
     * 批量删除用户
     * @param ids 用户id数组
     * @return 删除总数
     */
    int deleteByIdList(String[] ids);

    /**
     * 添加用户
     * @param user 用户对象
     * @return 新用户id 失败返回-1
     */
    int insertSelective(User user);

    /**
     * 重置用户密码
     * @param user 包含id | password | salt
     * @return update res
     */
    int updateUserPwd(User user);

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return 用户实体bean
     */
    User selectByPrimaryKey(Integer id);

    /**
     * 更新用户
     * @param user 用户对象
     * @return 更新行数
     */
    int updateByPrimaryKeySelective(User user);


    /**
     * 根据username获取user对象
     * @param userName 用户名
     * @return User对象
     */
    User selectUserByUserName(String userName);
}