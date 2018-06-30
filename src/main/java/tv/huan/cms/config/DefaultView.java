package tv.huan.cms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Project Name:BasicCMS
 * File Name:DefaultView
 *
 * @author wangyuxi
 * @date 2018/6/6 下午5:10
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Configuration
public class DefaultView implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/login.html");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);

    }
}

