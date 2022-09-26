package com.wuziqi.wuziqi.controller;

import com.wuziqi.wuziqi.service.user.Userinfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: 武鹏飞
 * @user:ASUS
 * @date:2022/9/26 - 15:06
 * @projectName:wuziqi
 */
@RestController
public class UserController {
    @Autowired
    private Userinfo userinfo;
    @PostMapping("/api/user/account/register/")
    public Map<String,String> register(@RequestParam Map<String,String>map){
        String username = map.get("username");
        String password = map.get("password");
        String confirmedPassword = map.get("confirmedPassword");
        return userinfo.register(username, password, confirmedPassword);
    }

    @PostMapping("/api/user/account/token/")
    public Map<String, String> getToken(@RequestParam Map<String, String>map){
        String username = map.get("username");
        String password = map.get("password");
        return userinfo.login(username, password);
    }
    @GetMapping("/api/user/account/info/")
    public Map<String, String> getinfo(){
        return userinfo.getInfo();
    }
}
