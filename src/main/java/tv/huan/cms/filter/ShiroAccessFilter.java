package tv.huan.cms.filter;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 自定义拦截用户未登录请求(区分浏览器请求与ajax请求)
 * Project Name:CMSFramework
 * File Name:ShiroAccessFilter
 *
 * @author wangyuxi
 * @date 2018/7/1 下午1:11.
 * Copyright (c) 2018, wangyuxi@huan.tv All Rights Reserved.
 */
@Slf4j
public class ShiroAccessFilter extends FormAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception{
        if(this.isLoginRequest(request, response)) {
            if(this.isLoginSubmission(request, response)) {
                return this.executeLogin(request, response);
            } else {
                return true;
            }
        } else {
            if(isAjax(request)){
                JSONObject resJson = new JSONObject();
                resJson.put("code","530");
                resJson.put("description","用户未登录或登录过期,请重新登录");
                ((HttpServletResponse)response).setHeader("Content-type", "text/html;charset=UTF-8");
                response.getWriter().print(resJson);
            }else{
                HttpSession session = ((HttpServletRequest) request).getSession();
                session.setAttribute("redirectFlag",1);
                this.saveRequest(request);
                WebUtils.issueRedirect(request, response, "/login.html?redirectFlag=1");
            }
            return false;
        }
    }

    private boolean isAjax(ServletRequest request){
        String ajaxFlag = "XMLHttpRequest";
        String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
        if(ajaxFlag.equalsIgnoreCase(header)){
            log.debug("当前请求为Ajax请求:url="+((HttpServletRequest) request).getRequestURI());
            return Boolean.TRUE;
        }else{
            log.debug("当前请求非Ajax请求:url="+((HttpServletRequest) request).getRequestURI());
            return Boolean.FALSE;
        }

    }
}
