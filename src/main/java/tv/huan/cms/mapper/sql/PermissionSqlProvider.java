package tv.huan.cms.mapper.sql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import tv.huan.cms.entity.Permission;

import java.util.Map;

/**
 * Project Name:BasicCMS
 * File Name:PermissionSqlProvider
 *
 * @author wangyuxi
 * @date 2018/6/25 下午3:32.
 * Copyright (c) 2018, wangyuxi@huan.tv All Rights Reserved.
 */
public class PermissionSqlProvider {

    public String selectPermission(Map<String,Object> para){
        return CommonSqlTools.commonPagingSql("permission",null,para);
    }

    public String insertPermission(@Param("permission")Permission permission){
        return CommonSqlTools.commonInsertSql("permission", (JSONObject) JSON.toJSON(permission));
    }

    public String deletePermission(@Param("ids")String[] ids){
        return CommonSqlTools.commonDeleteSql("permission",ids);
    }

    public String updatePermission(@Param("permission")Permission permission){
        return CommonSqlTools.commonUpdateSql("permission", (JSONObject) JSON.toJSON(permission));
    }
}
