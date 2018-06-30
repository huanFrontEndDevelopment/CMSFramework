package tv.huan.cms.mapper.sql;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import tv.huan.cms.entity.Menu;

/**
 * Project Name:BasicCMS
 * File Name:MenuSqlProvider
 *
 * @author wangyuxi
 * @date 2018/6/25 下午4:32.
 * Copyright (c) 2018, wangyuxi@huan.tv All Rights Reserved.
 */
public class MenuSqlProvider {

    public String selectMenu(@Param("level") int level, @Param("offset") int offset, @Param("pageSize") int pageSize, @Param("orderColumn") String orderColumn, @Param("orderSort") String orderSort){
        SQL sql = new SQL();
        sql.SELECT("*");
        sql.FROM("menu");
        sql.WHERE("level = " + level);
        if(pageSize!=0){
            sql.ORDER_BY(orderColumn + " " + orderSort);
            return sql.toString() + " LIMIT " + offset + "," + pageSize;
        }else{
            return sql.toString();
        }
    }

    public String insertMenu(@Param("menu") Menu menu){
        return CommonSqlTools.commonInsertSql("menu",(JSONObject) JSON.toJSON(menu));
    }

    public String deleteMenu(@Param("ids") String[] ids){
        return CommonSqlTools.commonDeleteSql("menu", ids);
    }

    public String updateMenu(@Param("menu") Menu menu){
        return CommonSqlTools.commonUpdateSql("menu",(JSONObject) JSON.toJSON(menu));
    }
}
