package com.jvblog.blogadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author javie
 * @date 2019/6/9 2:56
 */
@SpringBootApplication
@MapperScan({"com.jvblog.service.mapper","com.jvblog.blogadmin.mapper"})
public class BlogAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogAdminApplication.class, args);
    }
}
