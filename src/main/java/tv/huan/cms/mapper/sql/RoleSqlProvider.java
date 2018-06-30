package tv.huan.cms.mapper.sql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import tv.huan.cms.entity.Role;

import java.util.Map;

/**
 * Project Name:BasicCMS
 * File Name:RoleSqlProvider
 *
 * @author wangyuxi
 * @date 2018/6/23 下午3:22.
 * Copyright (c) 2018, wangyuxi@huan.tv All Rights Reserved.
 */
public class RoleSqlProvider {
    public String selectRole(Map<String,Object> para){
        return CommonSqlTools.commonPagingSql("role",null,para);
    }
    public String insertRole(@Param("role")Role role){
        return CommonSqlTools.commonInsertSql("role", (JSONObject) JSON.toJSON(role));
    }
    public String deleteRole(@Param("ids")String[] ids){
        return CommonSqlTools.commonDeleteSql("role", ids);
    }
    public String updateRole(@Param("role")Role role){
        return CommonSqlTools.commonUpdateSql("role",(JSONObject) JSON.toJSON(role));
    }
}
