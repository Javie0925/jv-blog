package com.jvblog.blogadmin.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author javie
 * @date 2019/6/16 15:25
 */
@Table(name = "tb_administrator")
@Data
public class Administrator {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private String username;

    private String password;

    private String salt;

    private Date createTime;

    private Date updateTime;

    private Date currentLoginTime;

    private Date lastLoginTime;

    private String currentLoginIp;

    private String lastLoginIp;

    private Integer loginCount = 0;

    private String telephone;

    private String email;

    private String userReason;

    private Boolean avilable = false;
}
