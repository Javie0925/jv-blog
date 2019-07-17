package com.jvblog.service.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Map;

/**
 * @author javie
 * @date 2019/6/6 18:46
 */
@Data
@Table(name = "tb_statistics")
public class Statistics {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private String ip;

    private String address;

    @Transient
    private Map<String,String> addrMap;
}
