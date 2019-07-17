package com.jvblog.blogadmin.controller;

import com.jvblog.blogadmin.config.JwtProperties;
import com.jvblog.blogadmin.service.AuthService;
import com.jvblog.service.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author javie
 * @date 2019/6/16 15:24
 */
@Controller
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties jwtProperties;
    @Value("${jv.jwt.cookieName}")
    private String cookieName;

    /**
     * 加载注册页面
     * @return
     */
    @GetMapping({"/register","/register.html"})
    public String loadRegisterPage(){
        return "register";
    }

    /**
     * 申请管理员
     * @param paramsMap
     * @return
     */
    @PostMapping("/auth/register")
    public ResponseEntity<Void> addAdministrator(@RequestBody Map<String,String> paramsMap){

        // 验证数据
        if (
                StringUtils.isEmpty(paramsMap.get("username"))
                ||StringUtils.isEmpty(paramsMap.get("password"))
                ||StringUtils.isEmpty(paramsMap.get("userReason"))
        ){
            // 空数据，返回错误信息
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        authService.addAdministrator(paramsMap);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public String loadLoginPage(){
        return "login";
    }

    /**
     * 登陆
     * @param userMap
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/auth/login")
    public ResponseEntity<Void> login(
            @RequestBody Map<String,String> userMap,
            HttpServletRequest request,
            HttpServletResponse response){

        // 验证数据
        if (
                StringUtils.isEmpty(userMap.get("username"))
                        ||StringUtils.isEmpty(userMap.get("password"))
                ){
            // 空数据，返回错误信息
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        // 登陆验证，生成token
        String token = authService.login(userMap,request.getHeader("X-Real-Ip"));
        // 登陆成功，将用户信息写入cookie
        CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token );

        return ResponseEntity.ok().build();
    }

    /**
     * 修改用户信息
     * @param paramsMap
     * @return
     */
    @PutMapping("/auth/")
    public ResponseEntity<Void> updateUserInfo(@RequestBody Map<String,String> paramsMap){
        authService.updateUserInfo(paramsMap);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/auth/signout")
    public ResponseEntity<Void> signOut(HttpServletRequest request,HttpServletResponse response){

        CookieUtils.deleteCookie(request, response, cookieName);
        return ResponseEntity.ok().build();
    }

}
