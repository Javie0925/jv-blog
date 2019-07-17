package com.jvblog.blogservice.config;

import com.jvblog.blogservice.interceptor.MyInterceptor;
import com.jvblog.blogservice.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author javie
 * @date 2019/6/3 0:14
 */
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Autowired
    private VisitorService visitorService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor(visitorService)).addPathPatterns("/**");
    }
}
