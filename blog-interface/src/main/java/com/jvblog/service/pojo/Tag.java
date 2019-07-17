package com.jvblog.service.pojo;

import lombok.Data;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author javie
 * @date 2019/6/4 13:20
 */
@Table(name = "tb_tag")
@Data
public class Tag {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer tagId;

    private String tagName;

    private Integer cid;

    @Transient
    private int blogCount;

    @Transient
    private String cname;
}
