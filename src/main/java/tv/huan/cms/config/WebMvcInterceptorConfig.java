package tv.huan.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tv.huan.cms.interceptor.LeftTreeInterceptor;

/**
 * Project Name:BasicCMS
 * File Name:WebMvcInterceptorConfig
 *
 * @author wangyuxi
 * @date 2018/6/7 上午10:55
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Configuration
public class WebMvcInterceptorConfig implements WebMvcConfigurer {
    @Bean
    public LeftTreeInterceptor leftTreeInterceptor(){
        return new LeftTreeInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册拦截器
        InterceptorRegistration ir = registry.addWebRequestInterceptor(leftTreeInterceptor());
        // 配置拦截的路径(拦截所有有菜单页面)
        ir.addPathPatterns("/*/*.do");
        // 退出登录不拦截
        ir.excludePathPatterns("/login/userLogout.do");
        // 默认入口不拦截
        ir.excludePathPatterns("/");
        ir.excludePathPatterns("/login.html");
    }


}

