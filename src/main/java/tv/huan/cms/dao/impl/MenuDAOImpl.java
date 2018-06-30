package tv.huan.cms.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tv.huan.cms.dao.IMenuDAO;
import tv.huan.cms.entity.Menu;
import tv.huan.cms.mapper.IMenuMapper;

import java.util.List;

/**
 * Project Name:BasicCMS
 * File Name:MenuDAOImpl
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:26
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Repository("menuDAO")
public class MenuDAOImpl implements IMenuDAO {

    @Autowired
    private IMenuMapper menuMapper;

    @Override
    public List<Menu> getAllMenu() {
        return menuMapper.selectAllMenu();
    }

    @Override
    public List<Menu> selectMenuList(int level, int offset, int pageSize, String orderColumn, String orderSort) {
        return menuMapper.selectMenuList(level, offset, pageSize, orderColumn, orderSort);
    }

    @Override
    public int insertSelective(Menu menu) {
        int res = menuMapper.insertSelective(menu);
        if(res == 1){
            return menu.getId();
        }else{
            return -1;
        }
    }

    @Override
    public int deleteByPrimaryKey(String[] ids) {
        return menuMapper.deleteByPrimaryKey(ids);
    }

    @Override
    public int updateByPrimaryKeySelective(Menu menu) {
        return menuMapper.updateByPrimaryKeySelective(menu);
    }

    @Override
    public Menu selectByPrimaryKey(Integer menuId) {
        return menuMapper.selectByPrimaryKey(menuId);
    }


    @Override
    public int selectMenuListCount(int level) {
        return menuMapper.selectMenuListCount(level);
    }

}

