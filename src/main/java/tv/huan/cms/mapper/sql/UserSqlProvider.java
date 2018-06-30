package tv.huan.cms.mapper.sql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import tv.huan.cms.entity.User;

import java.util.Map;

/**
 * Project Name:BasicCMS
 * File Name:UserSqlProvider
 *
 * @author wangyuxi
 * @date 2018/6/23 下午2:58
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Slf4j
public class UserSqlProvider {

    public String selectUser(Map<String,Object> para){
        return CommonSqlTools.commonPagingSql("user",null,para);
    }

    public String insertUser(@Param("user") User user){
        return CommonSqlTools.commonInsertSql("user", (JSONObject) JSON.toJSON(user));
    }

    public String deleteUserByIds(@Param("ids") final String[] ids){
        return CommonSqlTools.commonDeleteSql("user",ids);
    }

    public String updateUser(@Param("user") User user){
        return CommonSqlTools.commonUpdateSql("user",(JSONObject) JSON.toJSON(user));
    }
}
