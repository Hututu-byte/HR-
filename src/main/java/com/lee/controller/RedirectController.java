package com.lee.controller;

import com.lee.common.ResultObj;
import com.lee.shiro.ActiverUser;
import com.lee.util.WebUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RedirectController {
    @GetMapping("/{url}")
    public String url(@PathVariable("url") String url){
        if(url.equals(""))url = "index";
        return url;
    }


    @GetMapping("favicon.ico")
    @ResponseBody
    void returnNoFavicon() {
    }

    @PostMapping("/login")
    @ResponseBody
    public ResultObj login(String loginname, String password){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(loginname,password);
        try {
            subject.login(token);
            ActiverUser activerUser = (ActiverUser) subject.getPrincipal();
            WebUtils.getSession().setAttribute("user", activerUser.getUser());
            return ResultObj.LOGIN_SUCCESS;
        } catch (UnknownAccountException e) {
            return ResultObj.LOGIN_ERROR_LOGINNAME;
        } catch (IncorrectCredentialsException e) {
            return ResultObj.LOGIN_ERROR_PASS;
        }
    }


}
