package com.jvblog.service.mapper;

import com.jvblog.service.common.BaseMapper;
import com.jvblog.service.pojo.BlogDetail;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * @author javie
 * @date 2019/6/3 13:42
 */
public interface BlogDetailMapper extends BaseMapper<BlogDetail,Long> {

    @Select("SELECT * FROM tb_blog_detail WHERE tag_id=#{tagId} ORDER BY #{sortBy} desc")
    List<BlogDetail> queryBlogDetailListByTagIdDesc(
            @Param("tagId") Integer tagId,
            @Param("sortBy") String sortBy);

    @Select("SELECT * FROM tb_blog_detail WHERE tag_id=#{tagId} ORDER BY #{sortBy} asc")
    List<BlogDetail> queryBlogDetailListByTagIdAsc(
            @Param("tagId") Integer tagId,
            @Param("sortBy") String sortBy);
    @Select("SELECT COUNT(*) FROM tb_blog_detail")
    int selectBlogCount();

    @Select("SELECT watch FROM tb_blog_detail WHERE blog_id = #{blogId}")
    Integer selectWatchById(Long blogId);

    @Select("SELECT COUNT(*) FROM tb_blog_detail WHERE tag_id=#{tagId}")
    int selectByTagId(Integer tagId);

    @Select("SELECT title FROM tb_blog_detail WHERE blog_id = #{blogId}")
    String selectBlogTitleByBolgId(Long blogId);

    @Select("SELECT blog_id,title,comment_num FROM tb_blog_detail")
    List<Map<String,Object>> selectBlogIdAndTitlts();

    @Select("SELECT COUNT(*) FROM tb_blog_detail WHERE tag_id = #{tagId}")
    int selectBlogCountByTagId(Integer tagId);

    @Select("SELECT like_num FROM tb_blog_detail WHERE blog_id = #{blogId}")
    Integer selectLikeByBlogId(Long blogId);

    @Update("UPDATE tb_blog_detail SET like_num = #{likeNum} WHERE blog_id = #{blogId}")
    int increaceLikeNumById(@Param("likeNum") Integer likeNum,@Param("blogId") Long blogId);

    @Update("UPDATE tb_blog_detail SET watch = #{watch} WHERE blog_id = #{blogId}")
    int updateWatchByBlogId(@Param("watch") int watch,@Param("blogId")Long blogId);

    @Update("UPDATE tb_blog_detail SET visible = #{visible} WHERE blog_id =#{blogId}")
    int updateVisibleState(@Param("visible") boolean visible,@Param("blogId") Long blogId);

    @Select("SELECT visible FROM tb_blog_detail WHERE blog_id = #{blogId}")
    Boolean selectVisibleByBlogId(Long blogId);

}
