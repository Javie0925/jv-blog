package com.jvblog.service.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author javie
 * @date 2019/6/27 22:58
 */
@Data
@Table(name = "tb_comment")
public class Comment {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private String name;

    private String text;

    private Date createTime;

    private Long blogId;

    private Integer parentId = 0;

    private String ip;

    private String address;

    @Transient
    private String blogTitle;

}
