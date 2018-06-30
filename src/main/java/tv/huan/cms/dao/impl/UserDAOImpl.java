package tv.huan.cms.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tv.huan.cms.dao.IUserDAO;
import tv.huan.cms.entity.User;
import tv.huan.cms.mapper.IUserMapper;

import java.util.List;

/**
 * Project Name:BasicCMS
 * File Name:UserDAOImpl
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:34
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Repository("userDAO")
public class UserDAOImpl implements IUserDAO {
    @Autowired
    private IUserMapper userMapper;


    @Override
    public int deleteByIdList(String[] ids){

        return userMapper.deleteByIdList(ids);
    }



    @Override
    public int insertSelective(User user) {
        int res = userMapper.insertSelective(user);
        if(res == 1){
            return user.getId();
        }else{
            return -1;
        }
    }

    @Override
    public int updateUserPwd(User user) {
        return userMapper.updateUserPwd(user);
    }

    @Override
    public User selectByPrimaryKey(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public JSONArray selectUser(int offset, int pageSize, String orderColumn, String orderSort) {
        List<User> list = userMapper.selectUser(offset, pageSize, orderColumn, orderSort);
        return (JSONArray) JSON.toJSON(list);
    }

    @Override
    public int selectUserCount() {
        return userMapper.selectUserCount();
    }

    @Override
    public User selectUserByUserName(String userName) {
        return userMapper.selectUserByName(userName);
    }
}

