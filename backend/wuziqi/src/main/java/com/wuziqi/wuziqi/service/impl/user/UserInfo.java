package com.wuziqi.wuziqi.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wuziqi.wuziqi.mapper.UserMapper;
import com.wuziqi.wuziqi.pojo.User;
import com.wuziqi.wuziqi.service.user.Userinfo;
import com.wuziqi.wuziqi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 武鹏飞
 * @user:ASUS
 * @date:2022/9/26 - 14:59
 * @projectName:wuziqi
 */
@Service
public class UserInfo implements Userinfo {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String, String> map = new HashMap<>();
        if(username == null){
            map.put("error_message","用户名不能为空");
            return map;
        }
        if(password == null || confirmedPassword == null){
            map.put("error_message", "密码不能为空");
            return map;
        }
        username = username.trim();
        if(username.length() == 0){
            map.put("error_message","用户名不能为空");
            return map;
        }
        if (password.length() == 0 || confirmedPassword.length() == 0){
            map.put("error_message","密码长度不能为空");
            return map;
        }
        if (username.length()>100){
            map.put("error_message","用户名长度不能大于100");
            return map;
        }
        if (password.length()>100 || confirmedPassword.length() > 100){
            map.put("error_message","密码长度不能大于100");
            return map;
        }

        if(password.equals(confirmedPassword)==false){
            map.put("error_message", "两次密码不一致");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty()){
            map.put("error_message", "用户名已存在");
            return map;
        }

        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(null,username, encodedPassword);
        userMapper.insert(user);
        map.put("error_message","success");
        return map;
    }

    @Override
    public Map<String, String> login(String username, String password) {
        //加密
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        //验证吗，如果验证失败，自动处理异常
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        //验证成功后取出用户
        com.kob.backend.service.impl.utils.UserDetailsImpl loginUser = (com.kob.backend.service.impl.utils.UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();
        //封装为jwt token
        String jwt = JwtUtil.createJWT(user.getId().toString());
        //处理返回值
        Map<String, String> map = new HashMap<>();
        map.put("error_message","success");
        map.put("token",jwt);
        return map;
    }

    @Override
    public Map<String, String> getInfo() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        com.kob.backend.service.impl.utils.UserDetailsImpl loginUser = (com.kob.backend.service.impl.utils.UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        Map<String, String> map =new HashMap<>();
        map.put("error_message","success");
        map.put("id",user.getId().toString());
        map.put("username",user.getUsername());
        return map;
    }
}
