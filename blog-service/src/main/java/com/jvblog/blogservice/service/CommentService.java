package com.jvblog.blogservice.service;

import com.jvblog.service.enums.ExceptionEnum;
import com.jvblog.service.exception.BlogException;
import com.jvblog.service.mapper.BlogDetailMapper;
import com.jvblog.service.mapper.CommentMapper;
import com.jvblog.service.pojo.BlogDetail;
import com.jvblog.service.pojo.Comment;
import com.jvblog.service.utils.AddressUtils;
import com.jvblog.service.utils.IpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author javie
 * @date 2019/6/27 23:46
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private BlogDetailMapper blogDetailMapper;

    @Transactional
    public void createdComment(Comment comment,String ip) {
        try {
            //   [em_10]
            String str = comment.getText().replaceAll("\\[em_([0-9]*)\\]", "<img src='/face/$1.gif' border='0' style='width:24px;height:auto;vertical-align:baseline;padding:0;margin:0;outline:0;display:initial'/>");
            comment.setText(str);
            System.out.println(str);
        }catch (Exception e){

        }
        //获取ip地址
        String address = null;
        try {
            address = AddressUtils.getAddresses(ip);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 补齐comment数据
        comment.setIp(ip);
        if(StringUtils.isEmpty(comment.getName())){
            comment.setName("匿名");
        }
        comment.setAddress(address);
        comment.setCreateTime(new Date());
        // 保存comment
        int insert = commentMapper.insert(comment);
        if (insert==0){
            throw new BlogException(ExceptionEnum.COMMENT_CREATE_FAIL);
        }
        // 更新blogDetail的commentNum
        BlogDetail blogDetail = blogDetailMapper.selectByPrimaryKey(comment.getBlogId());
        blogDetail.setCommentNum(blogDetail.getCommentNum()+1);
        int update = blogDetailMapper.updateByPrimaryKey(blogDetail);
        if (update==0){
            throw new BlogException(ExceptionEnum.COMMENT_CREATE_FAIL);
        }
    }
}
