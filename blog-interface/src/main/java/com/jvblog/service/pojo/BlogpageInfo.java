package com.jvblog.service.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * @author javie
 * @date 2019/6/6 18:35
 */
@Data
@Table(name = "tb_blogpage_info")
public class BlogpageInfo {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private String blogName;

    private String headerLogo;

    private String links;

    private String footerMsg;

    private String headerMsg;

    private Long visit;

    private Long toppingBlogId;

    private String adminUrl;

    @Transient
    private BlogDetail blogDetail;

    @Transient
    private List<Category> categoryList;

}
