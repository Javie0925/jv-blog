package com.jvblog.service.vo;

import com.jvblog.service.pojo.*;
import lombok.Data;

import java.util.List;


/**
 * @author javie
 * @date 2019/6/3 22:44
 */
@Data
public class BlogVo {

    private Blog blog;

    private BlogDetail blogDetail;

    private List<BlogDetail> hotBlogs;

    private List<BlogDetail> recomends;

    private List<BlogDetail> relateds;

    private List<Tag> tagList;

    private BlogDetail pre;

    private BlogDetail next;

    private List<Comment> commentList;
}
