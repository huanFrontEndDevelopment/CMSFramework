package tv.huan.cms.config;


import org.apache.shiro.spring.web.ShiroFilterFactoryBean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Project Name:BasicCMS
 * File Name:MyShiroFilterFactoryBean
 *
 * @author wangyuxi
 * @date 2018/6/26 下午5:08.
 * Copyright (c) 2018, wangyuxi@huan.tv All Rights Reserved.
 */
public class MyShiroFilterFactoryBean extends ShiroFilterFactoryBean {


    /**
     * 初始化设置过滤链
     */
    @Override
    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        Map<String,String> filterMap = new LinkedHashMap<>();
        filterMap.put("/assets/**", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/login.html", "anon");
        filterMap.put("/logout", "logout");
        filterMap.put("/", "anon");
        filterMap.put("/login/checkUser.json", "anon");
        filterMap.put("/**", "authc");
        super.setFilterChainDefinitionMap(filterMap);
    }
}
