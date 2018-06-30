package tv.huan.cms.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 菜单管理service接口
 * Project Name:BasicCMS
 * File Name:MenuConfigService
 *
 * @author wangyuxi
 * @date 2018/6/8 下午1:31
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
public interface MenuConfigService {
    /**
     * 根据角色id获取目录树
     * @param roleId 角色ID
     * @return 目录树jsonobject {"data":[]}
     */
    JSONObject getMenuTree(String roleId);
    /**
     *  获取菜单分页列表
     * @param level 菜单层级
     * @param page 页码
     * @param pageSize 分页大小
     * @param sidx 排序列名
     * @param sort 排序依据
     * @param searchData 单条件查询条件json{field,value,oper}
     * @return JsonObject 分页对象
     */
    JSONObject getMenuList(int level, int page, int pageSize, String sidx, String sort, JSONObject searchData);
    /**
     * 添加菜单项
     * @param menuName menu名
     * @param menuIcon menu_icon
     * @param menuUrl menu跳转url
     * @param level 菜单层级
     * @param parentId 父节点id
     * @param pos menu位序
     * @return JSONObject {"code":200,"description":"success"}
     */
    JSONObject addMenu(String menuName, String menuIcon, String menuUrl, int level, int parentId, int pos);
    /**
     * 删除menu
     * @param ids 待删除主键数组(一个或者多个)
     * @return JSONObject {"code":200,"description":"success"}
     */
    JSONObject deleteMenu(String[] ids);
    /**
     * 更新Menu
     * @param id menuId
     * @param name menu名
     * @param icon menu_icon
     * @param url menu跳转url
     * @param pos menu同层级位序
     * @param level 菜单层级
     * @param parentId parent_id 父节点id
     * @return JSONObject {"code":200,"description":"success"}
     */
    JSONObject updateMenu(int id, String name, String icon, String url, Integer pos, Integer level, Integer parentId);
}
