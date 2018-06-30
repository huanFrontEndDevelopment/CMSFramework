package tv.huan.cms.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tv.huan.cms.entity.Role;
import tv.huan.cms.entity.User;
import tv.huan.cms.service.AccountService;
import tv.huan.cms.service.UserLoginService;

import java.util.List;

/**
 * Project Name:BasicCMS
 * File Name:UserLoginController
 *
 * @author wangyuxi
 * @date 2018/6/7 上午9:57
 * Copyright (c) 2016, wangyuxi@huan.tv All Rights Reserved.
 */
@Slf4j
@Controller
@RequestMapping("/login")
public class  UserLoginController {


    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private AccountService accountService;

    
    @RequestMapping("/goIndex.do")
    public String goIndex(){
        String goIndexFlag = "goIndex";
        SavedRequest savedRequest;
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        if(session != null){
            savedRequest =  (SavedRequest) session.getAttribute("shiroSavedRequest");
            if(savedRequest != null){
                String savedUrl = savedRequest.getRequestUrl();
                log.debug("===========last page = "+savedUrl);
                if(savedUrl.contains(goIndexFlag)){
                    return "index";
                }else{
                    session.removeAttribute("shiroSavedRequest");
                    return "redirect:"+savedUrl;
                }

            }else{
                log.debug("===========last page = null");
            }
        }
        return "index";
    }

    @RequestMapping("/checkUser.json")
    @ResponseBody
    public JSONObject checkUser(@RequestParam(name = "uname",required = true) String uname,
                                @RequestParam(name = "upasswd",required = true) String upasswd){
        UsernamePasswordToken token = new UsernamePasswordToken(uname,upasswd);
        Subject subject = SecurityUtils.getSubject();
        JSONObject msg = new JSONObject();
        try {
            subject.login(token);
        }catch (IncorrectCredentialsException ice){
            msg.put("code",90001);
            msg.put("description","password didn't match");
            log.error("password didn't match",ice);
            return msg;
        }catch (LockedAccountException lae){
            msg.put("code",90005);
            msg.put("description","user is locked");
            log.error("user is locked",lae);
            return msg;
        }catch (UnknownAccountException uae){
            msg.put("code",90002);
            msg.put("description","username wasn't in the system");
            log.error("username wasn't in the system",uae);
            return msg;
        }catch (ExcessiveAttemptsException eae){
            msg.put("code",90003);
            msg.put("description","times error!");
            log.error("times error!",eae);
            return msg;
        }catch (AuthenticationException e){
            log.error("AuthenticationException error!",e);
            msg.put("code",90004);
            msg.put("description","unexpected condition!");
            return msg;
        }
        User user = userLoginService.getUserByUserName(uname);
        List<Role> roleList = accountService.getRoleByUserName(uname);
        subject.getSession().setAttribute("user",user);
        if(roleList.size() >0){
            subject.getSession().setAttribute("role",roleList.get(0));
        }else{
            subject.getSession().setAttribute("role",null);
        }
        msg.put("code",1);
        msg.put("description","success!");
        return msg;
    }

}

