package tv.huan.cms.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tv.huan.cms.entity.Role;
import tv.huan.cms.service.MenuConfigService;

/**
 * 菜单管理controller
 * Project Name:BasicCMS
 * File Name:MenuConfigController
 *
 * @author wangyuxi
 * @date 2018/6/8 下午1:29
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Slf4j
@Controller
@RequestMapping("/menu")
public class MenuConfigController extends ExceptionController {
    @Autowired
    private MenuConfigService menuConfigService;
    @Value("${application.properties.superRoleName}")
    private String superRoleName;

    /**
     * 菜单数据获取
     * @return 菜单json数据
     */

    @RequestMapping("/leftTree.json")
    @ResponseBody
    public JSONObject getLeftTree(){
        Subject subject = SecurityUtils.getSubject();
        Role role = (Role)subject.getSession().getAttribute("role");
        if(superRoleName.equals(role.getName())){
            return menuConfigService.getMenuTree(null);
        }else{
            return menuConfigService.getMenuTree(role.getId().toString());
        }
    }

    /**
     * 一级菜单管理列表
     * @return moduleList
     */
    @RequiresPermissions("menu:1:view")
    @RequestMapping("/fLevelMenu.do")
    public String goFLevelMenuList(){
        return "fLevelMenu";
    }
    /**
     * 二级菜单管理列表
     * @return moduleList
     */
    @RequiresPermissions("menu:2:view")
    @RequestMapping("/sLevelMenu.do")
    public String goSLevelMenuList(){
        return "sLevelMenu";
    }
    /**
     * 三级菜单管理列表
     * @return moduleList
     */
    @RequiresPermissions("menu:3:view")
    @RequestMapping("/tLevelMenu.do")
    public String goTLevelMenuList(){
        return "tLevelMenu";
    }
    /**
     * 模块列表数据获取
     * @param page 页码
     * @param rows 分页大小 若为0,则查询全部
     * @param sidx 排序列名
     * @param sort 排序依据
     * @return aaData datatables表格数据
     */
    @RequestMapping("/data/menu.json")
    @ResponseBody
    public JSONObject getModuleListData(@RequestParam(name = "level",defaultValue = "1") int level,
                                        @RequestParam(name = "page",defaultValue = "1",required = false) int page,
                                        @RequestParam(name = "rows",defaultValue = "10",required = false) int rows,
                                        @RequestParam(name = "sidx") String sidx,
                                        @RequestParam(name = "sort") String sort,
                                        @RequestParam(name = "searchField",required = false) String searchField,
                                        @RequestParam(name = "searchString",required = false,defaultValue = "") String searchString,
                                        @RequestParam(name = "searchOper",required = false,defaultValue = "eq") String searchOper){
        Subject subject = SecurityUtils.getSubject();
        subject.checkPermission("menu:"+level+":view");
        return menuConfigService.getMenuList(level,page, rows,sidx,sort,null);
    }


    /**
     * JqGrid菜单类表格数据操作接口
     * @param oper 操作类型标识(add or del or update)
     * @param id menu id
     * @param name menu name
     * @param url menu url
     * @param icon menu parent id
     * @return json{"code","description"}
     */
    @RequestMapping("/menu/edit.json")
    @ResponseBody
    public JSONObject editMenu(@RequestParam(name = "oper") String oper,
                               @RequestParam(name = "id",required = false) String id,
                               @RequestParam(name = "name",required = false) String name,
                               @RequestParam(name = "url",required = false) String url,
                               @RequestParam(name = "icon",required = false) String icon,
                               @RequestParam(name = "level",required = false) Integer level,
                               @RequestParam(name = "parentId",required = false) Integer parentId,
                               @RequestParam(name = "pos",required = false) Integer pos){
        String defaultId = "_empty";
        JSONObject result = new JSONObject();
        Subject subject = SecurityUtils.getSubject();
        subject.checkPermission("menu:"+level+":"+oper);
        switch (oper){
            case "add": {
                //添加模块
                result = menuConfigService.addMenu(name,icon,url,level,parentId,pos);
            }break;
            case "del":{
                //批量删除检测符
                String multiFlag = ",";
                if(!defaultId.equals(id)){
                    String[] ids;
                    if(id.contains(multiFlag)){
                        ids = id.split(",");
                    }else{
                        ids = new String[1];
                        ids[0] = id;
                    }
                    result = menuConfigService.deleteMenu(ids);
                }
            }break;
            case "edit":{
                if(!defaultId.equals(id)) {
                    int updateMenuId = Integer.parseInt(id);
                    result = menuConfigService.updateMenu(updateMenuId, name, icon, url, pos, level, parentId);
                }
            }break;
            default:{

            }
        }
        return result;
    }
}
