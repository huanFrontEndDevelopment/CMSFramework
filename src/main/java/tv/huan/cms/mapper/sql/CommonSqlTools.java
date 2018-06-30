package tv.huan.cms.mapper.sql;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

/**
 * 共用查询方法sql语句生成模板
 * Project Name:BasicCMS
 * File Name:CommonSqlTools
 *
 * @author wangyuxi
 * @date 2018/6/23 下午3:07.
 * Copyright (c) 2018, wangyuxi@huan.tv All Rights Reserved.
 */
@Slf4j
public class CommonSqlTools {
    public static String commonPagingSql(String tableName,String[] selectParaList,Map<String,Object> para){
        Integer pageSize = (Integer) para.get("pageSize");
        SQL sql = new SQL();
        if(selectParaList == null){
            sql.SELECT("*");
        }else{
            for(String paraName : selectParaList){
                sql.SELECT(paraName);
            }
        }
        sql.FROM(tableName);
        if(pageSize != 0){
            sql.ORDER_BY(para.get("orderColumn") + " " + para.get("orderSort"));
            return sql.toString() + " LIMIT " + para.get("offset") + "," + para.get("pageSize");
        }else{
            return sql.toString();
        }
    }

    public static String commonInsertSql(String tableName,JSONObject json){
        SQL sql = new SQL();
        sql.INSERT_INTO(tableName);
        for (Map.Entry<String, Object> entry : json.entrySet()) {
            if(entry.getValue() != null){
                sql.VALUES(entry.getKey(),"'"+entry.getValue().toString()+"'");
            }
        }
        return sql.toString();
    }

    public static String commonDeleteSql(String tableName,String[] ids){
        SQL sql = new SQL();
        sql.DELETE_FROM(tableName);
        sql.WHERE("id IN("+arrayToString(ids)+")");
        return sql.toString();
    }

    public static String commonUpdateSql(String tableName,JSONObject json){
        SQL sql = new SQL();
        sql.UPDATE(tableName);
        int insertId = json.getIntValue("id");
        json.remove("id");
        for (Map.Entry<String, Object> entry : json.entrySet()) {
            if(entry.getValue() != null){
                sql.SET(entry.getKey() + " = '"+entry.getValue().toString()+"'");
            }
        }
        sql.WHERE("id = " + insertId);
        return sql.toString();
    }

    public static String arrayToString(String[] array){
        StringBuilder idString = new StringBuilder();
        for(String id : array){
            idString.append(id);
            idString.append(",");
        }
        idString.deleteCharAt(idString.length()-1);
        return idString.toString();
    }
}
