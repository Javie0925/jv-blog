package com.jvblog.blogservice.interceptor;

import com.jvblog.blogservice.service.VisitorService;
import com.jvblog.service.utils.AddressUtils;
import com.jvblog.service.utils.CookieUtils;
import com.jvblog.service.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @author javie
 * @date 2019/6/2 21:56
 */
@Slf4j
public class MyInterceptor implements HandlerInterceptor {

    private VisitorService visitorService;

    public MyInterceptor(VisitorService visitorService){
        this.visitorService = visitorService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取cookie里面的token
        String token = CookieUtils.getCookieValue(request, "jv_blog_token");
        // 验证token
        if(StringUtils.isNotEmpty(token)){
            // token存在，放行
            return true;
        }
        // 获取ip
        String ip = request.getHeader("X-Real-Ip");
        // 获取访客信息
        String address = null;
        try {
            address = AddressUtils.getAddresses(ip);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            // 存入访客信息表
            visitorService.storeVisitorInfo(ip,address,request.getHeader("User-Agent"));
            // 增加访客数量
            visitorService.addVisitor();
        }catch (Exception e){
            e.printStackTrace();
            // log.error("[访客服务] 访客信息保存失败，IP:{},Address:{}", ip,address);
            return true;
        }
        // 向用户cookie中添加token,30分钟过期
        CookieUtils.setCookie(request, response, "jv_blog_token", "welcome_to_jv_blog!", 1800);
        // 放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
