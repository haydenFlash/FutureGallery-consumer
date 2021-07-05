package com.futuregallery.workbench.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.futuregallery.model.User;
import com.futuregallery.service.UserService;
import com.futuregallery.utils.MD5Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/workbench")
public class IndexController {
    @Reference(interfaceClass = UserService.class, version = "1.0.0", check = false)
    private UserService userService;

    @RequestMapping("/index")
    public String index() {
        return "workbench/index";
    }

    @RequestMapping("/checkPwd")
    @ResponseBody
    public Map<String, Object> checkPwd(String pwd, HttpServletRequest request) {
        String loginName = ((User) request.getSession().getAttribute("user")).getLoginName();
        User user = new User();
        user.setLoginName(loginName);
        user.setLoginPwd(MD5Util.getMD5(pwd));

        return userService.checkPwd(user);
    }

    @RequestMapping("/editPwd")
    @ResponseBody
    public Boolean editPwd(String newPwd, HttpServletRequest request) {
        String id = ((User) request.getSession().getAttribute("user")).getId();

        User user = new User();
        user.setId(id);
        user.setLoginPwd(MD5Util.getMD5(newPwd));

        return userService.editPwd(user);
    }
}
