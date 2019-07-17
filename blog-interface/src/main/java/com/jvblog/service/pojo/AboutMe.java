package com.jvblog.service.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author javie
 * @date 2019/6/6 18:41
 */
@Table(name = "tb_about_me")
@Data
public class AboutMe {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private String nickname;

    private String avator;

    private String job;

    private String contact;

    private String summary;

    private String html;

}
