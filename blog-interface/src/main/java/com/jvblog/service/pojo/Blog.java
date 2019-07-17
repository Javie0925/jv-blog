package com.jvblog.service.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author javie
 * @date 2019/6/3 13:33
 */
@Data
@Table(name = "tb_blog")
public class Blog {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long blogId;

    private Long userId;

    private Integer categoryId;

}
