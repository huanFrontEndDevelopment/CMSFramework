package tv.huan.cms.controller;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


/**
 * 通用异常处理
 * Project Name:BasicCMS
 * File Name:ExceptionController
 *
 * @author wangyuxi
 * @date 2018/6/27 上午10:37.
 * Copyright (c) 2018, wangyuxi@huan.tv All Rights Reserved.
 */
@Slf4j
public class ExceptionController {

    @ExceptionHandler(UnauthorizedException.class)
    public ModelAndView unauthorizedExceptionHandler(UnauthorizedException exception, HttpServletRequest request){
        String reqUrl = request.getRequestURI();
        String jsonFlag = ".json";
        ModelAndView view;
        if(reqUrl.contains(jsonFlag)){
            view = new ModelAndView(new FastJsonJsonView());
            view.addObject("description",exception.getMessage());
            view.addObject("code",403);
        }else{
            request.getSession().setAttribute("ex",exception.getMessage());
            view = new ModelAndView("403");
        }
        log.error("无权限访问=====================");
        return view;
    }
}
