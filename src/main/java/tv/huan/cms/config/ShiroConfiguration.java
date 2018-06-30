package tv.huan.cms.config;

import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tv.huan.cms.schedule.QuartzSessionValidationScheduler2;
import tv.huan.cms.shiro.RetryLimitHashedCredentialsMatcher;
import tv.huan.cms.shiro.UserRealm;

import java.util.LinkedHashMap;

/**
 * Project Name:BasicCMS
 * File Name:ShiroConfiguration
 * Created by wangyuxi on 2018/6/7 上午10:18.
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Configuration
public class ShiroConfiguration {


    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.port}")
    private int port;
    /**
     * shiro redisManager
     * @return RedisManager
     */
    @Bean(name="redisManager")
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        // 配置缓存过期时间
        redisManager.setExpire(1800);
        redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * cacheManager
     * @param manager RedisManager
     * @return RedisCacheManager
     */
    @Bean(name="cacheManager")
    public RedisCacheManager cacheManager(@Qualifier("redisManager") RedisManager manager){
        RedisCacheManager cacheManager = new RedisCacheManager();
        cacheManager.setRedisManager(manager);
        return cacheManager;
    }

    /**
     * 配置自定义的密码比较器
     * @param cacheManager RedisCacheManager
     * @return RetryLimitHashedCredentialsMatcher
     */
    @Bean(name="credentialsMatcher")
    public RetryLimitHashedCredentialsMatcher credentialsMatcher(@Qualifier("cacheManager") RedisCacheManager cacheManager) {
        RetryLimitHashedCredentialsMatcher retryLimitHashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher(cacheManager);
        retryLimitHashedCredentialsMatcher.setHashAlgorithmName("md5");
        retryLimitHashedCredentialsMatcher.setHashIterations(2);
        retryLimitHashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return retryLimitHashedCredentialsMatcher;
    }

    /**
     * 配置自定义的权限登录器
     * @param matcher CredentialsMatcher
     * @return UserRealm
     */
    @Bean(name="userRealm")
    public UserRealm userRealm(@Qualifier("credentialsMatcher") CredentialsMatcher matcher) {
        UserRealm userRealm=new UserRealm();
        userRealm.setCredentialsMatcher(matcher);
        return userRealm;
    }

    /**
     * 会话ID生成器
     * @return JavaUuidSessionIdGenerator
     */
    @Bean(name = "sessionIdGenerator")
    public JavaUuidSessionIdGenerator sessionIdGenerator(){
        return new JavaUuidSessionIdGenerator();
    }

    /**
     * 会话Cookie模板
     * @return  SimpleCookie
     */
    @Bean(name = "sessionIdCookie")
    public SimpleCookie sessionIdCookie(){
        SimpleCookie cookie = new SimpleCookie("jeesite.session.id");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        return cookie;
    }

    /**
     * remember 会话cookie
     * @return SimpleCookie
     */
    @Bean(name = "rememberMeCookie")
    public SimpleCookie rememberMeCookie(){
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setHttpOnly(true);
        //30天
        cookie.setMaxAge(2592000);
        return cookie;
    }

    /**
     * rememberMe管理器
     * @param rememberMeCookie SimpleCookie
     * @return CookieRememberMeManager
     */
    @Bean(name = "rememberMeManager")
    public CookieRememberMeManager rememberMeManager(@Qualifier("rememberMeCookie") SimpleCookie rememberMeCookie){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        cookieRememberMeManager.setCookie(rememberMeCookie);
        return cookieRememberMeManager;
    }


    /**
     * 会话验证调度器
     * @param sessionManager 会话管理器
     * @return QuartzSessionValidationScheduler2
     */
    @Bean(name = "sessionValidationScheduler")
    public QuartzSessionValidationScheduler2 sessionValidationScheduler(@Qualifier("sessionManager") DefaultWebSessionManager sessionManager){
        QuartzSessionValidationScheduler2 sessionValidationScheduler = new QuartzSessionValidationScheduler2();
        sessionValidationScheduler.setSessionValidationInterval(1800000);
        sessionValidationScheduler.setSessionManager(sessionManager);
        return sessionValidationScheduler;
    }

    /**
     * redisSessionDAO
     * @param redisManager shiro redisManager
     * @return RedisSessionDAO
     */
    @Bean(name = "redisSessionDAO")
    public RedisSessionDAO redisSessionDAO(@Qualifier("redisManager") RedisManager redisManager){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager);
        return redisSessionDAO;
    }

    /**
     * 会话管理器
     * @param sessionIdCookie 会话Cookie模板
     * @return DefaultWebSessionManager
     */
    @Bean(name = "sessionManager")
    public DefaultWebSessionManager sessionManager(@Qualifier("sessionIdCookie")SimpleCookie sessionIdCookie, @Qualifier("redisSessionDAO")RedisSessionDAO redisSessionDAO){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);
        sessionManager.setSessionIdCookie(sessionIdCookie);
        sessionManager.setSessionDAO(redisSessionDAO);
        return sessionManager;
    }

    /**
     * 安全管理器
     * @param userRealm Realm实现
     * @param sessionManager 会话管理器
     * @param cacheManager cache管理器
     * @param rememberMeManager rememberMe管理器
     * @return DefaultWebSecurityManager
     */
    @Bean(name="securityManager")
    public DefaultWebSecurityManager securityManager(@Qualifier("userRealm") UserRealm userRealm,
                                                     @Qualifier("sessionManager")DefaultWebSessionManager sessionManager,
                                                     @Qualifier("cacheManager") RedisCacheManager cacheManager,
                                                     @Qualifier("rememberMeManager") CookieRememberMeManager rememberMeManager) {
        DefaultWebSecurityManager manager=new DefaultWebSecurityManager();
        manager.setRealm(userRealm);
        manager.setSessionManager(sessionManager);
        manager.setCacheManager(cacheManager);
        manager.setRememberMeManager(rememberMeManager);
        return manager;
    }

    /**
     * 相当于调用SecurityUtils.setSecurityManager(securityManager)
     * @param securityManager 安全管理器
     * @return MethodInvokingFactoryBean
     */
    @Bean(name = "methodInvokingFactoryBean")
    public MethodInvokingFactoryBean methodInvokingFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        methodInvokingFactoryBean.setArguments(securityManager);
        return methodInvokingFactoryBean;
    }


    /**
     * Shiro的Web过滤器
     * @param securityManager 安全管理器
     * @return ShiroFilterFactoryBean
     */
    @Bean(name="shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        MyShiroFilterFactoryBean bean=new MyShiroFilterFactoryBean();
        bean.setSecurityManager( securityManager);
        //未登录跳转到登录页
        bean.setLoginUrl("/error/530.html");
        //无权限跳转
        bean.setUnauthorizedUrl("/error/403.html");
        //配置访问权限
        LinkedHashMap<String, String> filterChainDefinitionMap=new LinkedHashMap<>();
        bean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return bean;
    }

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator creator=new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }
}

