package tv.huan.cms.mapper.sql;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import tv.huan.cms.entity.UserRole;

import java.util.Map;

/**
 * 分页查询用户角色关系sql生成类
 * Project Name:BasicCMS
 * File Name:UserRoleSqlProvider
 *
 * @author wangyuxi
 * @date 2018/6/23 下午2:27
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Slf4j
public class UserRoleSqlProvider {

    public String selectUserRoleList(Map<String,Object> para){
        return CommonSqlTools.commonPagingSql("user_role",null,para);
    }

    public String insertUserRole(@Param("userRole")UserRole userRole){
        SQL sql = new SQL();
        sql.INSERT_INTO("user_role");
        sql.VALUES("user_id",userRole.getUser().getId().toString());
        sql.VALUES("role_id",userRole.getRole().getId().toString());
        return sql.toString();
    }

    public String deleteUserRole(@Param("ids")String[] ids){
        return CommonSqlTools.commonDeleteSql("user_role",ids);
    }

    public String updateUserRole(@Param("id")Integer id, @Param("userId")Integer userId, @Param("roleId")Integer roleId){
        SQL sql = new SQL();
        sql.UPDATE("user_role");
        if(userId != null){
            sql.SET("user_id = "+userId);
        }
        if(roleId != null){
            sql.SET("role_id = "+roleId);
        }
        sql.WHERE("id = "+id);
        return sql.toString();

    }
}
