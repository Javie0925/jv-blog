package com.jvblog.service.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author javie
 * @date 2019/6/3 22:25
 */
@Table(name = "tb_user")
@Data
public class User {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long userId;

    private String userName;

    private int age;

    private String gender;

    private String avator;

    private String about;
}
