package com.jvblog.blogadmin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jvblog.service.enums.ExceptionEnum;
import com.jvblog.service.exception.BlogException;
import com.jvblog.service.mapper.BlogDetailMapper;
import com.jvblog.service.mapper.BlogMapper;
import com.jvblog.service.mapper.TagMapper;
import com.jvblog.service.pojo.BlogDetail;
import com.jvblog.service.pojo.Tag;
import com.jvblog.service.utils.JsonUtils;
import com.jvblog.service.utils.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

/**
 * @author javie
 * @date 2019/6/9 3:04
 */
@Service
public class BlogService {

    @Autowired
    private BlogDetailMapper blogDetailMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private BlogMapper blogMapper;

    /**
     * 按照关键字和分页参数查询博客列表
     * @param pageNum
     * @param pageSize
     * @param sortBy
     * @param key
     * @param desc
     * @return
     */
    public PageResult<BlogDetail> queryBlogDetailsByKeyAndPage(
            Integer pageNum,Integer pageSize,String sortBy,Integer tagId,String key,Boolean desc){

        // 分页
        PageHelper.startPage(pageNum, pageSize);
        // 过滤
        Example example = new Example(BlogDetail.class);
        if(StringUtils.isNotBlank(key)){
            example.createCriteria().
                    orLike("title", "%"+key+"%");
        }
        // 根据标签查找
        if (tagId!=null&&tagId!=0){
            example.createCriteria().andEqualTo("tagId", tagId);
        }
        // 排序
        if(StringUtils.isNotBlank(sortBy)){
            String orderByClause = sortBy + (desc?" desc":" asc");
            example.setOrderByClause(orderByClause);
        }
        //去除不需要的字段
        example.excludeProperties("content");
        // 查询
        List<BlogDetail> blogDetails = blogDetailMapper.selectByExample(example);
        // 判空
        if(CollectionUtils.isEmpty(blogDetails)){
            throw new BlogException(ExceptionEnum.BLOG_NOT_FOUND);
        }
        // 补充tag
        blogDetails.forEach(detail->{
            Tag tag = tagMapper.selectByPrimaryKey(detail.getTagId());
            if (tag!=null) {
                detail.setTag(tag.getTagName());
            }else{
                detail.setTag("个人博客");
            }
        });
        PageInfo<BlogDetail> pageInfo = new PageInfo<>(blogDetails);

        Long totalPage = pageInfo.getTotal()%pageSize==0?pageInfo.getTotal()/pageSize:pageInfo.getTotal()/pageSize+1;

        return new PageResult<BlogDetail>(pageInfo.getTotal(),totalPage,blogDetails,pageNum,pageSize);
    }

    /**
     * 根据id删除博客
     * @param blogId
     */
    @Transactional
    public void deleteBlogById(Long blogId) {
        // 删除BlogDetail
        int i = blogDetailMapper.deleteByPrimaryKey(blogId);
        if (i==0){
            throw new BlogException(ExceptionEnum.BLOG_DELETE_FAIL);
        }
    }
    @Transactional
    public void deleteBlogByIds(List<Long> blogIds) {
        // 删除blogDetail
        int i = blogDetailMapper.deleteByIdList(blogIds);
        if (i==0){
            throw new BlogException(ExceptionEnum.BLOG_DELETE_FAIL);
        }
    }

    /**
     * 获取博客详细内容
     * @param blogId
     * @return
     */
    public BlogDetail queryBlogDetailById(Long blogId){
        // 获取blogDetail
        BlogDetail blogDetail = blogDetailMapper.selectByPrimaryKey(blogId);
        // 判空
        if(blogDetail==null){
            throw new BlogException(ExceptionEnum.BLOG_NOT_FOUND);
        }
        // 获取tag
        Tag tag = tagMapper.selectByPrimaryKey(blogDetail.getTagId());
        blogDetail.setTag(tag.getTagName());

        return blogDetail;
    }

    /**
     * 加载所有标签
     * @return
     */
    public List<Tag> queryTagList(){

        return tagMapper.selectAll();
    }

    /**
     * 修改博客
     * @param blogDetail
     */
    @Transactional
    public void updateBlog(BlogDetail blogDetail) {

        // 补充数据
        blogDetail.setUpdateTime(new Date());
        // 更新
        int update = blogDetailMapper.updateByPrimaryKeySelective(blogDetail);
        if (update==0){
            throw new BlogException(ExceptionEnum.BLOG_UPDATE_FAIL);
        }
    }

    /**
     * 修改博客状态
     * @param blogId
     */
    @Transactional
    public void updateBlogStateById(Long blogId) {
        // 获取
        Boolean visible = blogDetailMapper.selectVisibleByBlogId(blogId);
        // 判断
        if(visible==null){
            throw new BlogException(ExceptionEnum.BLOG_STATE_UPDATE_FAIL);
        }
        // 修改状态
        visible = !visible;
        // 保存
        int update = blogDetailMapper.updateVisibleState(visible,blogId);
        // 判断
        if (update==0){
            throw new BlogException(ExceptionEnum.BLOG_STATE_UPDATE_FAIL);
        }
    }

    /**
     * 添加博客
     * @param blogDetail
     */
    @Transactional
    public void addArticle(BlogDetail blogDetail) {
        // 判断数据
        if (
                StringUtils.isEmpty(blogDetail.getTitle())
                ||StringUtils.isEmpty(blogDetail.getSummary())
        ){

            throw new BlogException(ExceptionEnum.BLOG_ADD_FAIL);
        }
        // 补充数据
        blogDetail.setCreateTime(new Date());
        blogDetail.setLikeNum(0);
        blogDetail.setWatch(0);
        int insert = blogDetailMapper.insertSelective(blogDetail);
        if (insert==0){
            throw new BlogException(ExceptionEnum.BLOG_ADD_FAIL);
        }
    }

    /**
     * cms论评页面所需内容
     * @return
     */
    public List<BlogDetail> getBlogIdAndTitleByBlogId(){

        List<Map<String, Object>> mapList = blogDetailMapper.selectBlogIdAndTitlts();
        List<BlogDetail> blogList = new ArrayList<>();
        mapList.forEach(map->{
            BlogDetail blogDetail = new BlogDetail();
            blogDetail.setBlogId((Long)map.get("blog_id"));
            blogDetail.setTitle((String)map.get("title"));
            blogDetail.setCommentNum((Integer) map.get("comment_num"));
            blogList.add(blogDetail);
        });
        // 去除无评论博客
        for (int i=0;i<blogList.size();){
            BlogDetail blogDetail = blogList.get(i);
            if (blogDetailMapper.selectByPrimaryKey(blogDetail.getBlogId()).getCommentNum()==0){
                blogList.remove(blogDetail);
                continue;
            }
            i++;
        }
        return blogList;
    }

    /**
     * CMS标签页，查询标签博客数量
     * @param tagId
     * @return
     */
    public int selectBlogCountByTagId(Integer tagId) {
        int blogCount = blogDetailMapper.selectBlogCountByTagId(tagId);
        return blogCount;
    }
}
