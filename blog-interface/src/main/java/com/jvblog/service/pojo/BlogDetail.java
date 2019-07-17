package com.jvblog.service.pojo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author javie
 * @date 2019/6/3 13:37
 */
@Data
@Table(name = "tb_blog_detail")
public class BlogDetail {

    @Id
    @Column(name = "blog_id")
    private Long blogId;

    private String title;

    @Column(name = "profile_image")
    private String profileImage;

    private String summary;

    private String content;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    private Integer watch;

    @Column(name = "tag_id")
    private Integer tagId;

    private Boolean visible = true;

    private String author;

    private Integer commentNum = 0;

    private Integer likeNum = 0;

    private Boolean original;

    private String originalUrl;

    @Transient
    private String tag;

}
