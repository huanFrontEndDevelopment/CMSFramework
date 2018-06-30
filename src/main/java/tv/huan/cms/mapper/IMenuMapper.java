package tv.huan.cms.mapper;

import org.apache.ibatis.annotations.*;
import tv.huan.cms.entity.Menu;
import tv.huan.cms.mapper.sql.MenuSqlProvider;

import java.util.List;

/**
 * Project Name:BasicCMS
 * File Name:IMenuMapper
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:27
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Mapper
public interface IMenuMapper {

    /**
     * 获取所有Menu对象
     * @return 所有Menu列表
     */
    @Select("select * from menu")
    List<Menu> selectAllMenu();

    /**
     * 分页查询MENU对象
     * @param level 模块/功能
     * @param offset 起始位置
     * @param pageSize 偏移量
     * @param orderColumn 排序字段
     * @param orderSort 排序方式
     * @return Menu列表
     */
    @SelectProvider(type = MenuSqlProvider.class, method = "selectMenu")
    List<Menu> selectMenuList(@Param("level") int level, @Param("offset") int offset, @Param("pageSize") int pageSize, @Param("orderColumn") String orderColumn, @Param("orderSort") String orderSort);
    /**
     * 获取Menu长度
     * @param level 菜单级别
     * @return Menu长度
     */
    @Select("select count(id) from menu where level = #{level}")
    int selectMenuListCount(@Param("level") int level);

    /**
     * 添加新Menu
     * @param menu 新菜单对象
     * @return 受影响行数
     */
    @InsertProvider(type = MenuSqlProvider.class, method = "insertMenu")
    @Options(useGeneratedKeys = true,keyProperty = "menu.id")
    int insertSelective(@Param("menu") Menu menu);

    /**
     * 根据主键id删除menu
     * @param ids 主键id数组
     * @return 删除操作受影响行数
     */
    @DeleteProvider(type = MenuSqlProvider.class, method = "deleteMenu")
    int deleteByPrimaryKey(@Param("ids") String[] ids);

    /**
     * 更新菜单信息
     * @param menu 菜单对象
     * @return 更新数据库受影响行数(成功 1)
     */
    @UpdateProvider(type = MenuSqlProvider.class, method = "updateMenu")
    int updateByPrimaryKeySelective(Menu menu);

    /**
     * 根据id查询menu对象
     * @param id 主键id
     * @return 主键id对应的menu对象
     */
    @Select("select * from menu where id = #{id}")
    Menu selectByPrimaryKey(Integer id);

}
