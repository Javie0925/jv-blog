package com.jvblog.service.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author javie
 * @date 2019/6/10 12:14
 */
@Table(name = "tb_visitor")
@Data
public class Visitor {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String ip;

    private String address;

    private Date visitTime;

    private String userAgent;

}
