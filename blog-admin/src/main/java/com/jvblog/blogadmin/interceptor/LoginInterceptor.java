package com.jvblog.blogadmin.interceptor;

import com.jvblog.blogadmin.config.JwtProperties;
import com.jvblog.blogadmin.pojo.UserInfo;
import com.jvblog.blogadmin.utils.JwtUtils;
import com.jvblog.service.utils.CookieUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author javie
 * @date 2019/6/2 21:56
 */
public class LoginInterceptor implements HandlerInterceptor {

    private JwtProperties jwtProperties;

    private static final ThreadLocal<UserInfo> JVBLOG_THREAD_LOCAL = new ThreadLocal<>();

    public LoginInterceptor(JwtProperties jwtProperties){
        this.jwtProperties = jwtProperties;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("#$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        System.out.println(request.getRequestURL());
        // 获取token
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        try {
            // 解析token
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            // 写入request
            request.setAttribute("userInfo", userInfo);
            // 写入ThreadLocal
            JVBLOG_THREAD_LOCAL.set(userInfo);
            // 刷新token,重新生成token
            String newToken = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(),jwtProperties.getExpire());
            // 写入cookie
            CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), newToken);

        } catch (Exception e) {
            e.printStackTrace();
            // 解析token失败，重定向到登陆页面
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        
    }


    public static UserInfo getUserInfo(){
        return JVBLOG_THREAD_LOCAL.get();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 用完数据，清空数据
        JVBLOG_THREAD_LOCAL.remove();
    }
}
