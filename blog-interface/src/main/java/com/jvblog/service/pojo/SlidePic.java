package com.jvblog.service.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author javie
 * @date 2019/6/14 0:05
 */
@Table(name = "tb_slide_pics")
@Data
public class SlidePic {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private String url;

    private String text;

    private String link;

    private Boolean visible = true;
}
