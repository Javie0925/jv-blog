package com.jvblog.service.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author javie
 * @date 2019/6/5 14:52
 */
@Data
@Table(name = "tb_message")
public class Message {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String nickname;

    private String email;

    private String text;

    private String address;

    private String ip;

    private Date createTime;
}
