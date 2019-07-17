package com.jvblog.blogadmin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jvblog.service.enums.ExceptionEnum;
import com.jvblog.service.exception.BlogException;
import com.jvblog.service.mapper.BlogDetailMapper;
import com.jvblog.service.mapper.CommentMapper;
import com.jvblog.service.pojo.BlogDetail;
import com.jvblog.service.pojo.Comment;
import com.jvblog.service.utils.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author javie
 * @date 2019/7/1 12:04
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private BlogDetailMapper blogDetailMapper;

    public PageResult<Comment> queryCommentsByKeyAndPage(
            Integer pageNum,
            Integer pageSize,
            String sortBy,
            Long blogId,
            Boolean desc
    ){

        // 分页
        PageHelper.startPage(pageNum, pageSize);
        // 过滤
        Example example = new Example(BlogDetail.class);
        if(blogId!=null && blogId!=0){
            example.createCriteria().
                    andEqualTo("blogId", blogId);
        }
        // 排序
        if(StringUtils.isNotBlank(sortBy)){
            String orderByClause = sortBy + (desc?" desc":" asc");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        List<Comment> commentList = commentMapper.selectByExample(example);
        // 判空
        if(CollectionUtils.isEmpty(commentList)){
            throw new BlogException(ExceptionEnum.COMMENT_NOT_FOUND);
        }
        // 添加博客名
        commentList.forEach(comment -> {
            String blogTitle = blogDetailMapper.selectBlogTitleByBolgId(comment.getBlogId());
            comment.setBlogTitle(blogTitle);
        });
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);

        Long totalPage = pageInfo.getTotal()%pageSize==0?pageInfo.getTotal()/pageSize:pageInfo.getTotal()/pageSize+1;

        return new PageResult<Comment>(pageInfo.getTotal(),totalPage,commentList,pageNum,pageSize);
    }

    /**
     * 删除单个
     * @param id
     */
    @Transactional
    public void deleteById(Integer id) {

        //修改blogDetail的commentNum
        Comment comment = commentMapper.selectByPrimaryKey(id);
        BlogDetail blogDetail = blogDetailMapper.selectByPrimaryKey(comment.getBlogId());
        blogDetail.setCommentNum(blogDetail.getCommentNum()-1);
        int update = blogDetailMapper.updateByPrimaryKey(blogDetail);
        if (update==0){
            throw new BlogException(ExceptionEnum.COMMENT_DELETE_FAIL);
        }
        //删除comment
        int delete = commentMapper.deleteByPrimaryKey(id);
        if (delete==0){
            throw new BlogException(ExceptionEnum.COMMENT_DELETE_FAIL);
        }
    }

    /**
     * 批量删除
     * @param ids
     */
    @Transactional
    public void deleteByIds(List<Integer> ids) {
        //修改blogDetail的commentNum
        ids.forEach(id->{
            Comment comment = commentMapper.selectByPrimaryKey(id);
            BlogDetail blogDetail = blogDetailMapper.selectByPrimaryKey(comment.getBlogId());
            blogDetail.setCommentNum(blogDetail.getCommentNum()-1);
            int update = blogDetailMapper.updateByPrimaryKey(blogDetail);
            if (update==0){
                throw new BlogException(ExceptionEnum.COMMENT_DELETE_FAIL);
            }
        });
        //删除comment
        int delete = commentMapper.deleteByIdList(ids);
        if (delete==0){
            throw new BlogException(ExceptionEnum.COMMENT_DELETE_FAIL);
        }
    }
}
