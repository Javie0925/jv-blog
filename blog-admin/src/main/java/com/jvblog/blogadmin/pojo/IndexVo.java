package com.jvblog.blogadmin.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author javie
 * @date 2019/6/17 21:37
 */
@Data
public class IndexVo {

    private int blogCount;

    private int commentCount;

    private int flinkCount;

    private int visitorCount;

    private int loginCount;

    private int administratorCount;

    private Date lastLoginTime;

    private String lastLoginIp;

    private String currentLoginIp;
}
