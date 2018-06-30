package tv.huan.cms.interceptor;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;
import tv.huan.cms.entity.Role;
import tv.huan.cms.service.MenuConfigService;

/**
 * Project Name:BasicCMS
 * File Name:LeftTreeInterceptor
 *
 * @author wangyuxi
 * @date 2018/6/7 上午10:52
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Slf4j
@Component
public class LeftTreeInterceptor implements WebRequestInterceptor {

    @Autowired
    private MenuConfigService menuConfigService;
    @Value("${application.properties.superRoleName}")
    private String superRoleName;

    @Override
    public void preHandle(WebRequest webRequest) throws Exception {


    }

    @Override
    public void postHandle(WebRequest webRequest, @Nullable ModelMap modelMap) throws Exception {
        log.debug("=======菜单数据拦截器开始执行=========");
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        if(modelMap != null){
            Role role = (Role)subject.getSession().getAttribute("role");
            JSONObject treeJson;
            if(superRoleName.equals(role.getName())){
                treeJson = menuConfigService.getMenuTree(null);
            }else{
                treeJson = menuConfigService.getMenuTree(role.getId().toString());
            }
            modelMap.addAttribute("leftTree",treeJson);
        }
        if (principalCollection == null){
            log.info("会话过期,需要重新登录");
            log.debug("=======菜单数据拦截器结束=========");
        }else{
            log.debug("=======菜单数据填充成功=========");
        }



    }

    @Override
    public void afterCompletion(WebRequest webRequest, @Nullable Exception e) throws Exception {

    }
}
