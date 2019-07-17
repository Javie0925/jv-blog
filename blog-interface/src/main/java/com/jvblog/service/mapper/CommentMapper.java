package com.jvblog.service.mapper;

import com.jvblog.service.common.BaseMapper;
import com.jvblog.service.pojo.Comment;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author javie
 * @date 2019/6/27 23:03
 */
public interface CommentMapper extends BaseMapper<Comment,Integer> {

    @Select("SELECT * FROM tb_comment WHERE blog_id = #{blogId} ORDER BY create_time DESC")
    List<Comment> queryCommentsByBlogId(Long blogId);

    @Select("SELECT COUNT(*) FROM tb_comment")
    int selectAllCount();
}
