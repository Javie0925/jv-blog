package com.jvblog.blogadmin.service;

import com.jvblog.service.enums.ExceptionEnum;
import com.jvblog.service.exception.BlogException;
import com.jvblog.service.mapper.BlogDetailMapper;
import com.jvblog.service.mapper.TagMapper;
import com.jvblog.service.pojo.BlogDetail;
import com.jvblog.service.pojo.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author javie
 * @date 2019/6/12 23:57
 */
@Service
public class TagService {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private BlogDetailMapper blogDetailMapper;

    public List<Tag> queryTagList(){
        List<Tag> tagList = tagMapper.selectAll();
        //添加blogCount
        tagList.forEach(tag->{
            int blogCount = blogDetailMapper.selectBlogCountByTagId(tag.getTagId());
            tag.setBlogCount(blogCount);
        });
        return tagList;
    }

    @Transactional
    public void deleteByTagIds(List<Integer> tagIds) {
        int delete = tagMapper.deleteByIdList(tagIds);
        if (delete==0){
            throw new BlogException(ExceptionEnum.TAG_DELETE_FAIL);
        }
    }
    @Transactional
    public void deleteTagById(Integer tagId) {
        BlogDetail blogDetail = new BlogDetail();
        blogDetail.setTagId(tagId);
        List<BlogDetail> blogDetailList = blogDetailMapper.select(blogDetail);
        if(blogDetailList!=null&&blogDetailList.size()>0){
            throw new BlogException(ExceptionEnum.TAG_IS_NOT_BLANK);
        }
        int delete = tagMapper.deleteByPrimaryKey(tagId);
        if (delete==0){
            throw new BlogException(ExceptionEnum.TAG_DELETE_FAIL);
        }
    }

    @Transactional
    public void updateTag(Tag tag) {
        if(StringUtils.isEmpty(tag.getTagName())){
            throw new BlogException(ExceptionEnum.TAG_NAME_ERROR);
        }
        if (tagMapper.selectByPrimaryKey(tag.getTagId())==null) {
            throw new BlogException(ExceptionEnum.TAG_NOT_FOUND);
        }
        int update = tagMapper.updateByPrimaryKey(tag);
        if (update==0){
            throw new BlogException(ExceptionEnum.TAG_UPDATE_FAIL);
        }
    }

    public void addTag(String tagName,Integer cid) {
        if (StringUtils.isEmpty(tagName)){
            throw new BlogException(ExceptionEnum.TAG_NAME_ERROR);
        }
        Tag tag = new Tag();
        tag.setTagName(tagName);
        tag.setCid(cid);
        int insert = tagMapper.insert(tag);
        if (insert==0){
            throw new BlogException(ExceptionEnum.TAG_ADD_FAIL);
        }
    }

    /**
     * 获得分类下标签的数量
     * @param cid
     * @return
     */
    public int selectCountByCid(Integer cid){
        int i = tagMapper.selectCountByCid(cid);
        return i;
    }
}
