package tv.huan.cms.mapper.sql;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import tv.huan.cms.entity.RolePermission;

import java.util.Map;

/**
 * Project Name:BasicCMS
 * File Name:RolePermissionSqlProvider
 *
 * @author wangyuxi
 * @date 2018/6/25 下午5:07.
 * Copyright (c) 2018, wangyuxi@huan.tv All Rights Reserved.
 */
public class RolePermissionSqlProvider {

    public String selectRolePermission(Map<String,Object> para){
        return CommonSqlTools.commonPagingSql("role_permission",null,para);
    }

    public String insertRolePermission(@Param("rolePermission")RolePermission rolePermission){
        SQL sql = new SQL();
        sql.INSERT_INTO("role_permission");
        sql.VALUES("permission_id",rolePermission.getPermission().getId().toString());
        sql.VALUES("role_id",rolePermission.getRole().getId().toString());
        return sql.toString();
    }

    public String batchInsert(@Param("roleId") String roleId, @Param("permissionIds") String[] permissionIds){
        StringBuilder sb = new StringBuilder();
        sb.append("insert into role_permission (role_id,permission_id) values ");
        for(String permissionId : permissionIds){
            sb.append("(");
            sb.append(roleId);
            sb.append(",");
            sb.append(permissionId);
            sb.append(")");
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public String upateRolePermission(@Param("rolePermission")RolePermission rolePermission){
        SQL sql = new SQL();
        sql.UPDATE("role_permission");
        if(rolePermission.getRole().getId() != null){
            sql.SET("role_id = "+rolePermission.getRole().getId());
        }
        if(rolePermission.getPermission().getId() != null){
            sql.SET("permission_id = "+rolePermission.getPermission().getId());
        }
        sql.WHERE("id = "+rolePermission.getId());
        return sql.toString();
    }

    public String deleteRolePermission(@Param("ids") String[] ids){
        return CommonSqlTools.commonDeleteSql("role_permission",ids);
    }
}
