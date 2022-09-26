package com.wuziqi.wuziqi.service.user;

import java.util.Map;

/**
 * @author: 武鹏飞
 * @user:ASUS
 * @date:2022/9/26 - 14:57
 * @projectName:wuziqi
 */
public interface Userinfo {
    Map<String,String> register(String username, String password, String confirmedPassword);
    Map<String,String> login(String username, String password);
    Map<String,String> getInfo();
}
