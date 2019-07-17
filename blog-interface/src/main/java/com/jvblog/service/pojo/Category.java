package com.jvblog.service.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author javie
 * @date 2019/6/3 22:29
 */
@Table(name = "tb_category")
@Data
public class Category {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer cid;

    private String cname;

    @Transient
    private int tagCount;
}
