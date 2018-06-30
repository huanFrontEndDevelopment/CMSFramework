package tv.huan.cms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tv.huan.cms.dao.IMenuDAO;
import tv.huan.cms.dao.IPermissionDAO;
import tv.huan.cms.entity.Menu;
import tv.huan.cms.service.MenuConfigService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单管理Service实现
 * Project Name:BasicCMS
 * File Name:MenuConfigServiceImpl
 *
 * @author wangyuxi
 * @date 2018/6/8 下午1:31
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Service("menuConfigService")
@Slf4j
public class MenuConfigServiceImpl implements MenuConfigService {
    /**
     * 返回消息JSONObject
     */
    private JSONObject serviceMsg;
    @Autowired
    private IMenuDAO menuDAO;
    @Autowired
    private IPermissionDAO permissionDAO;
    @Value("${application.properties.homePageUrl}")
    private String homePageUrl;

    /**
     * 构建返回json
     * @param code 返回code
     * @param desc 返回描述
     * @return {"code":code,"description":desc}
     */
    private JSONObject createReturnMsg(int code, String desc){
        serviceMsg = new JSONObject();
        serviceMsg.put("code",code);
        serviceMsg.put("description",desc);
        return serviceMsg;
    }
    private List<Menu> filterMenu(List<Menu> all, List<String> permArray){
        List<Menu> resultList = new ArrayList<>(10);
        for(Menu menu : all){
            String url = menu.getUrl();
            //有子节点Menu保留(待优化)
            if(url.isEmpty()){
                resultList.add(menu);
            }else{
                //有权限访问Menu保留
                if(permArray.contains(url)){
                    resultList.add(menu);
                }
            }
        }

        return resultList;
    }

    private List<Menu> cleanEmpty(List<Menu> all){
        List<Menu> firstLevelMenuList = new ArrayList<>();
        List<Menu> secondLevelMenuList = new ArrayList<>();
        List<Menu> thirdLevelMenuList = new ArrayList<>();
        for(Menu menu : all){
            if(menu.getLevel() == 1){
                firstLevelMenuList.add(menu);
            }
            if(menu.getLevel() == 2){
                secondLevelMenuList.add(menu);
            }
            if(menu.getLevel() == 3){
                thirdLevelMenuList.add(menu);
            }
        }

        List<Menu> removeSecondList = getRemoveList(secondLevelMenuList,thirdLevelMenuList);
        List<Menu> removeFirstList = getRemoveList(firstLevelMenuList,secondLevelMenuList);
        all = removeList(removeSecondList,all);
        all = removeList(removeFirstList,all);
        return all;
    }

    private List<Menu> removeList(List<Menu> removeList, List<Menu> all){
        for(Menu menu : removeList){
            if(!homePageUrl.equals(menu.getUrl())){
                all.remove(menu);
            }

        }
        return all;
    }

    private List<Menu> getRemoveList(List<Menu> parentList, List<Menu> childList){
        List<Menu> removeList = new ArrayList<>();
        for(Menu menu : parentList){
            boolean childFlag = false;
            for(Menu child : childList){
                if(child.getParentId().equals(menu.getId())){
                    childFlag = true;
                }
            }
            if(!childFlag){
                removeList.add(menu);
            }
        }
        return removeList;
    }

    @Override
    public JSONObject getMenuTree(String roleId) {
        try{
            List<Menu> listAll = menuDAO.getAllMenu();
            if(roleId != null){
                List<String> permissionUrlList = permissionDAO.selectRolePermissionUrlByRoleId(roleId);
                permissionUrlList.add(homePageUrl);
                listAll = filterMenu(listAll,permissionUrlList);
                listAll = cleanEmpty(listAll);
            }
            //获取父分类列表
            List<Menu> menuTree = listAll.stream().filter(menu -> 1 == menu.getLevel()).collect(Collectors.toList());
            //根据pos排序rootTree
            Collections.sort(menuTree, (a, b) -> a.getPos().compareTo(b.getPos()));
            for(Menu menu : menuTree){
                menu.setList(getChild(menu.getId(),listAll));
            }
            JSONObject moduleTree = new JSONObject();
            moduleTree.put("data",menuTree);
            return moduleTree;
        }catch (Exception e){
            log.error("获取目录树出错",e);
            return null;
        }
    }

    /**
     * 递归获取id对应menu下所有子menu
     * @param id 父id
     * @param listAll 所有子menu
     * @return 子菜单树
     */
    private List<Menu> getChild(int id, List<Menu> listAll){
        List<Menu> childList = new ArrayList<>();
        for (Menu menu : listAll) {
            int menuParentId = menu.getParentId();
            if(0 != menuParentId){
                if(menuParentId == id){
                    childList.add(menu);
                }
            }
        }
        for (Menu menu : childList) {
            String url = menu.getUrl();
            if(url.isEmpty()){
                menu.setList(getChild(menu.getId(),listAll));
            }
        }
        if(childList.size() == 0){
            return null;
        }
        return childList;
    }

    @Override
    public JSONObject getMenuList(int level , int page, int pageSize, String sidx, String sort, JSONObject searchData) {
        JSONObject menuList = new JSONObject();
        menuList.put("page",page);
        try {
            List<Menu> list = menuDAO.selectMenuList(level,((page-1)*pageSize),pageSize,sidx,sort);
            int totalModuleCount = menuDAO.selectMenuListCount(level);
            menuList.put("records",totalModuleCount);
            if(pageSize == 0){
                menuList.put("total",0);
            }else{
                menuList.put("total",totalModuleCount%pageSize == 0 ? totalModuleCount/pageSize : (totalModuleCount/pageSize + 1));
            }
            menuList.put("rows", JSONArray.parseArray(JSON.toJSONString(list)));
        }catch (Exception e){
            log.error("获取模块列表出错",e);
        }
        return menuList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public JSONObject addMenu(String menuName, String menuIcon, String menuUrl, int level, int parentId, int pos) {
        try {
            Menu menu = new Menu();
            menu.setName(menuName);
            menu.setIcon(menuIcon);
            menu.setUrl(menuUrl);
            menu.setLevel(level);
            menu.setParentId(parentId);
            menu.setPos(pos);
            int newMenuId = menuDAO.insertSelective(menu);
            log.debug("新增"+level+"级菜单成功,菜单id:"+newMenuId);
            if(level == 3){
                //新增菜单为三级菜单,需要将父菜单URL置为空;
                Menu parentMenu = menuDAO.selectByPrimaryKey(parentId);
                log.debug("父菜单信息:id="+parentId+",name="+parentMenu.getName());
                if(!parentMenu.getUrl().isEmpty()){
                    log.debug("父菜单URL不为空,将置空");
                    parentMenu.setUrl("");
                    menuDAO.updateByPrimaryKeySelective(parentMenu);
                }
            }
            serviceMsg = createReturnMsg(1,"add menu success");
        }catch (Exception e){
            log.error("添加menu出错",e);
            serviceMsg = createReturnMsg(90101, "add menu error");
        }
        return serviceMsg;
    }

    @Override
    public JSONObject deleteMenu(String[] ids) {
        try {
            int deleteResult = menuDAO.deleteByPrimaryKey(ids);
            if(deleteResult == 1){
                serviceMsg = createReturnMsg(1,"delete menu success");
            }else{
                serviceMsg = createReturnMsg(90102,"delete menu failed");
            }
        }catch (Exception e){
            log.error("删除menu出错",e);
            serviceMsg = createReturnMsg(90103,"delete menu error");
        }
        return serviceMsg;
    }


    @Override
    public JSONObject updateMenu(int id, String name, String icon, String url, Integer pos, Integer level, Integer parentId) {
        try {
            Menu menu = new Menu();
            menu.setId(id);
            menu.setName(name);
            menu.setIcon(icon);
            menu.setUrl(url);
            menu.setPos(pos);
            menu.setLevel(level);
            menu.setParentId(parentId);
            int updateResult = menuDAO.updateByPrimaryKeySelective(menu);
            if(updateResult == 1){

                serviceMsg = createReturnMsg(1,"update menu success");
            }else{
                serviceMsg = createReturnMsg(90104,"update menu failed");
            }

        }catch (Exception e){
            log.error("更新menu出错",e);
            serviceMsg = createReturnMsg(90105,"update menu error");
        }
        return serviceMsg;
    }
}
