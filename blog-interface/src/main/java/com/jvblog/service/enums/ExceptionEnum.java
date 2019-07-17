package com.jvblog.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author javie
 * @date 2019/6/3 13:57
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    BAD_REQUEST_PARAMS(400,"请求参数错误"),
    BLOG_NOT_FOUND(404,"博客未找到。"),
    INSERT_MESSAGE_FAIL(500,"创建留言失败"),
    BLOG_DELETE_FAIL(500,"博客删除失败"),
    MESSAGE_NOT_FOUND(500,"加载留言失败"),
    MESSAGE_DELETE_FAIL(500,"删除留言失败"),
    VISITOR_UPDATE_FAIL(500,"访客信息记录失败"),
    WATCH_INCR_FAIL(500,"访问数据增加失败"),
    BLOG_UPDATE_FAIL(500,"更新博客失败"),
    BLOG_INVISIBLE(500,"博客加密，拒绝访问"),
    TAG_DELETE_FAIL(500,"标签删除失败"),
    TAG_NOT_FOUND(404,"标签不存在"),
    TAG_UPDATE_FAIL(500,"修改标签失败"),
    TAG_NAME_ERROR(400,"标签名不能为空"),
    TAG_ADD_FAIL(500,"标签添加失败"),
    TAG_IS_NOT_BLANK(500,"标签不为空，不可删除"),
    LINK_ADD_FAIL(500,"添加链接失败"),
    LINK_DELETE_FAIL(500,"链接删除失败"),
    HEADER_MSG_UPDATE_FAIL(500,"修改失败"),
    SLIDE_PIC_UPDATE_FAIL(500,"滚动图信息修改失败"),
    SLIDE_PIC_ADD_FAIL(500,"滚动图添加失败"),
    SLIDE_PIC_DEL_FAIL(500,"滚动图删除失败"),
    BLOGPAGE_INFO_UPDATE_FAIL(500,"页面信息修改失败"),
    BLOG_STATE_UPDATE_FAIL(500,"博客状态修改失败"),
    VISITOR_DELETE_FAIL(500,"访客信息删除失败"),
    BLOG_ADD_FAIL(500,"文章添加失败"),
    ABOUTME_UPDATE_FAIL(500,"关于我信息修改失败"),
    ABOUTME_ADD_FAIL(500,"关于我信息添加失败"),
    ADMINISTRATOR_ADD_FAIL(500,"管理员添加失败"),
    LOGIN_FAIL(500,"登陆失败"),
    TOKEN_CREATE_ERROR(500,"生成用户token失败"),
    INVALID_USERNAME(500,"用户名已存在"),
    WRONG_PASSWORD(500,"密码不正确"),
    USERINFO_UPDATE_FAIL(500,"用户信息修改失败"),
    COMMENT_CREATE_FAIL(500,"添加评论失败"),
    COMMENT_NOT_FOUND(400,"评论未找到"),
    COMMENT_DELETE_FAIL(500,"评论删除失败"),
    LIKE_ADD_FAIL(500,"点赞添加失败"),
    CATEGORY_DELETE_FAIL(500,"栏目删除失败"),
    CATEGORY_UPDATE_FAIL(500,"栏目修改失败"),
    CATEGORY_ADD_FAIL(500,"栏目添加失败"),
    CATEGORY_NOT_FOUND(404,"栏目添加失败"),

    ;
    private int code;
    private String msg;

}
