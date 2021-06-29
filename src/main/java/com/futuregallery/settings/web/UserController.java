package com.futuregallery.settings.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.futuregallery.exception.LoginException;
import com.futuregallery.model.User;
import com.futuregallery.service.UserService;
import com.futuregallery.utils.MD5Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Reference(interfaceClass = UserService.class, version = "1.0.0", check = false)
    private UserService userService;

    @RequestMapping(value = "/adminLogin")
    @ResponseBody
    public Object login(String loginName, String loginPwd, HttpServletRequest request) {
        //将密码转换为密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        Map<String, Object> map = new HashMap<>();

        //调用业务方法得到User对象，未报错就将其储存到session域，返回true，报错了就返回报错信息和false
        try {
            User user = userService.login(loginName, loginPwd);
            request.getSession().setAttribute("user", user);
            map.put("success", true);
            return map;
        } catch (LoginException e) {
            String msg = e.getMessage();
            map.put("success", false);
            map.put("msg", msg);
            return map;
        }
    }

//    @RequestMapping("/test")
//    public String test(Model model) {
//        model.addAttribute("test", "doTest");
//
//        return "workbench/contacts/test";
//    }
}
