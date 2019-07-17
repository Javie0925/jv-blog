package com.jvblog.blogadmin.config;

import com.jvblog.blogadmin.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author javie
 * @date 2019/6/3 0:14
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class MyMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor(jwtProperties))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/fonts/**",
                        "/config.json",
                        "/",
                        "/login",
                        "/login.html",
                        "/register",
                        "/register.html",
                        "/auth/register",
                        "/auth/login",
                        "/js/**",
                        "/css/**",
                        "/images/**");
    }
}
