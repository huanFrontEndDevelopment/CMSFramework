package tv.huan.cms.dao;

import tv.huan.cms.entity.Menu;

import java.util.List;

/**
 * Project Name:BasicCMS
 * File Name:IMenuDAO
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:26
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
public interface IMenuDAO {
    /**
     * 获取所有菜单item
     * @return list
     */
    List<Menu> getAllMenu();

    /**
     * 分页查询MENU对象
     * @param level 菜单层级
     * @param offset 起始位置
     * @param pageSize 偏移量
     * @param orderColumn 排序字段
     * @param orderSort 排序方式
     * @return Menu列表
     */
    List<Menu> selectMenuList(int level, int offset, int pageSize, String orderColumn, String orderSort);
    /**
     * 获取Menu长度
     * @param level 模块/功能
     * @return Menu长度
     */
    int selectMenuListCount(int level);

    /**
     * 添加Menu
     * @param menu menu对象
     * @return 新菜单对象id(插入失败返回-1)
     */
    int insertSelective(Menu menu);

    /**
     * 根据主键删除Menu
     * @param ids 主键id列表
     * @return success:1
     */
    int deleteByPrimaryKey(String[] ids);

    /**
     * 可选Menu参数更新
     * @param menu menu对象
     * @return 受影响行数
     */
    int updateByPrimaryKeySelective(Menu menu);


    /**
     * 根据主键id查询Menu对象
     * @param id 主键id
     * @return id对应menu对象
     */
    Menu selectByPrimaryKey(Integer id);


}
