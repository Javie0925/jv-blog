package com.jvblog.blogservice.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jvblog.service.enums.ExceptionEnum;
import com.jvblog.service.exception.BlogException;
import com.jvblog.service.mapper.*;
import com.jvblog.service.pojo.*;
import com.jvblog.service.utils.PageResult;
import com.jvblog.service.vo.BlogVo;
import com.jvblog.service.vo.MsgVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author javie
 * @date 2019/6/2 22:27
 */
@Service
@Slf4j
public class BlogService {

    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private BlogDetailMapper blogDetailMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private BlogpageInfoMapper blogpageInfoMapper;
    @Autowired
    private AboutMeMapper aboutMeMapper;
    @Autowired
    private SlidePicMapper slidePicMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Value("${jv.blog.hotBlogPageSize}")
    private Integer hotBlogPageSize;

    public void loadIndexData() {
        // 查询分页博客列表
        // 查询作者信息
        // 查询标签
        // 查询点击排行博客列表
        // 查询推荐文章
    }

    /**
     * 条件，排序，分页查询博客列表
     * @return
     */
    public PageResult<BlogDetail> queryBlogDetailList(
            Integer pageNum,Integer pageSize,String sortBy,String key,Boolean desc){

        // 分页
        PageHelper.startPage(pageNum, pageSize);
        // 过滤
        Example example = new Example(BlogDetail.class);
        if(StringUtils.isNotBlank(key)){
            example.createCriteria().
                    orLike("title", "%"+key+"%").
                    andEqualTo("visible",true);// 剔除加密博客
        }
        // 排序
        if(StringUtils.isNotBlank(sortBy)){
            String orderByClause = sortBy + (desc?" desc":" asc");
            example.setOrderByClause(orderByClause);
        }
        // 去除不需要的字段
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
     * 根据id查询blog详情页所需信息
     * @param id
     * @return
     */
    @Transactional
    public BlogVo queryBlogById(Long id) {
        // 查询blog表
        Blog blog = blogMapper.selectByPrimaryKey(id);
        // 查询blogdetail表，并增加访问次数
        BlogDetail blogDetail = null;
        try {
            blogDetail = getBlogDetailThenIncrWatch(id);
        }catch (Exception e){
            log.error("增加watch失败，blogId:{}", id);
        }
        // 判断是否可见
        if (!blogDetail.getVisible()){
            throw new BlogException(ExceptionEnum.BLOG_INVISIBLE);
        }
        blogDetail.setTag(tagMapper.selectByPrimaryKey(blogDetail.getTagId()).getTagName());
        // 查询点击排行
        PageResult<BlogDetail> pageResult = queryBlogDetailList(1, hotBlogPageSize, "watch", null, true);
        List<BlogDetail> hotBlogs = pageResult.getItemList();
        // 获取推荐阅读
        PageResult<BlogDetail> pageResult1 = queryBlogDetailListByTagId(1, 10, "create_time", blogDetail.getTagId(), false);
        // 获取tagList
        List<Tag> tagList = tagMapper.selectAll();
        // 剔除没有内容的标签
        for (int i=0;i<tagList.size();){
            Tag tag = tagList.get(i);
            int blogCount = blogDetailMapper.selectByTagId(tag.getTagId());
            if(blogCount==0){
                tagList.remove(tag);
                continue;
            }
            tag.setBlogCount(blogCount);
            i++;
        }
        // 获取上一篇
        BlogDetail pre = queryBlogDetailExcludeContentByBlogId(id - 1);
        //获取下一篇
        BlogDetail next = queryBlogDetailExcludeContentByBlogId(id + 1);
        //获取评论
        List<Comment> commentList = queryCommentsByBlogId(id);
        // 封装数据
        BlogVo blogVo = new BlogVo();
        blogVo.setBlog(blog);
        blogVo.setBlogDetail(blogDetail);
        blogVo.setHotBlogs(hotBlogs);
        blogVo.setTagList(tagList);
        blogVo.setRecomends(pageResult1.getItemList());
        blogVo.setPre(pre);
        blogVo.setNext(next);
        blogVo.setCommentList(commentList);
        return blogVo;
    }
    public BlogDetail queryBlogDetailExcludeContentByBlogId(Long blogId){
        Example example = new Example(BlogDetail.class);
        example.createCriteria().andEqualTo("blogId", blogId);
        example.excludeProperties("content");
        BlogDetail blogDetail = blogDetailMapper.selectOneByExample(example);
        return blogDetail;
    }

    public List<Tag> queryTagList() {

        List<Tag> tagList = tagMapper.selectAll();
        // 剔除没有内容的标签,添加内容计数
        for (int i=0;i<tagList.size();){
            Tag tag = tagList.get(i);
            int blogCount = blogDetailMapper.selectByTagId(tag.getTagId());
            if(blogCount==0){
                tagList.remove(tag);
                continue;
            }
            tag.setBlogCount(blogCount);
            i++;
        }
        return tagList;
    }

    /**
     * 根据标签查询blog
     * @param pageNum
     * @param pageSize
     * @param sortBy
     * @param tagId
     * @param desc
     * @return
     */
    public PageResult<BlogDetail> queryBlogDetailListByTagId(
            Integer pageNum,Integer pageSize,String sortBy,Integer tagId,Boolean desc){

        // 分页
        PageHelper.startPage(pageNum, pageSize);
        // 过滤
        Example example = new Example(BlogDetail.class);
        example.createCriteria().andEqualTo("tagId", tagId).
                andEqualTo("visible", true);// 剔除加密博客
        // 排序
        if(StringUtils.isNotBlank(sortBy)){
            String orderByClause = sortBy + (desc?" desc":" asc");
            example.setOrderByClause(orderByClause);
        }
        // 去除不需要的字段
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
     * 条件，排序，分页查询博客列表
     * @return
     */
    public PageResult<BlogDetail> queryBlogDetailListByZone(
            Integer pageNum,Integer pageSize,String sortBy,String key,String zone,Boolean desc){

        // 分页
        PageHelper.startPage(pageNum, pageSize);
        // 过滤
        Example example = new Example(BlogDetail.class);
        if(StringUtils.isNotBlank(key)){
            example.createCriteria().
                    orLike(zone, "%"+key+"%").
                    andEqualTo("visible", true);// 剔除加密博客
        }
        // 排序
        if(StringUtils.isNotBlank(sortBy)){
            String orderByClause = sortBy + (desc?" desc":" asc");
            example.setOrderByClause(orderByClause);
        }
        // 去除不需要的字段
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

    public Boolean searchCheck(String keyword) {
        Example example = new Example(BlogDetail.class);
        if(StringUtils.isNotBlank(keyword)){
            example.createCriteria().
                    orLike("title", "%"+keyword+"%");
        }
        // 指定查询字段
        example.selectProperties("title");
        List<BlogDetail> blogDetails = blogDetailMapper.selectByExample(example);
        // 判空
        if(CollectionUtils.isEmpty(blogDetails)){
            return false;
        }
        return true;
    }

    public Tag queryTagById(Integer tagId) {
        Tag tag = tagMapper.selectByPrimaryKey(tagId);
        return tag;
    }

    /**
     * 分页查询留言
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult<Message> queryMessageByPage(Integer pageNum,Integer pageSize) {
        // 分页
        PageHelper.startPage(pageNum, pageSize);
        // 排序
        Example example = new Example(Message.class);
        String orderByClause = "create_time desc";
        example.setOrderByClause(orderByClause);
        // 查询
        List<Message> messageList = messageMapper.selectByExample(example);

        PageInfo<Message> pageInfo = new PageInfo<>(messageList);

        Long totalPage = pageInfo.getTotal()%pageSize==0?pageInfo.getTotal()/pageSize:pageInfo.getTotal()/pageSize+1;
        return new PageResult<Message>(pageInfo.getTotal(),totalPage,messageList,pageNum,pageSize);
    }

    /**
     * 保存留言信息
     * @param msgVo
     * @param address
     */
    @Transactional
    public void createMessage(MsgVo msgVo, String ip, String address) {
        Message message = new Message();
        String nickname = msgVo.getNickname();
        if (StringUtils.isEmpty(nickname)){
            nickname = "匿名";
        }
        message.setNickname(nickname);
        message.setEmail(msgVo.getEmail());
        message.setText(msgVo.getText());
        message.setCreateTime(new Date());
        message.setAddress(address);
        message.setIp(ip);
        int insert = messageMapper.insertSelective(message);
        if (insert==0){
            throw new BlogException(ExceptionEnum.INSERT_MESSAGE_FAIL);
        }
    }

    public BlogpageInfo queryBlogpageInfo() {
        BlogpageInfo blogpageInfo = blogpageInfoMapper.selectByPrimaryKey(1);
        // 获取置顶文章内容
        BlogDetail blogDetail = queryBlogDetailExcludeContentByBlogId(blogpageInfo.getToppingBlogId());
        blogpageInfo.setBlogDetail(blogDetail);
        return blogpageInfo;
    }

    public AboutMe queryAboutMe() {

        AboutMe aboutMe = aboutMeMapper.selectByPrimaryKey(1);
        return aboutMe;
    }

    /**
     * 获取blogDetail并增加watch数量
     * @param blogId
     * @return
     */
    @Transactional
    public BlogDetail getBlogDetailThenIncrWatch(Long blogId){
        // 获取detail
        Example example = new Example(BlogDetail.class);
        example.createCriteria().orEqualTo("blogId", blogId);
        BlogDetail blogDetail = blogDetailMapper.selectOneByExample(example);
        if (blogDetail==null){
            throw new BlogException(ExceptionEnum.BLOG_NOT_FOUND);
        }
        // 查likeNum
        // blogDetail.setLikeNum(blogDetailMapper.selectLikeByBlogId(blogId));
        // 更新数据
        int update = blogDetailMapper.updateWatchByBlogId(blogDetail.getWatch()+1,blogId);
        if (update==0){
            throw new BlogException(ExceptionEnum.WATCH_INCR_FAIL);
        }
        return blogDetail;
    }

    public List<SlidePic> querySlidePicList() {
        List<SlidePic> slidePicList = slidePicMapper.selectVisible();
        return slidePicList;
    }

    /**
     * 根据blogId查询评论列表
     * @param blogId
     * @return
     */
    public List<Comment> queryCommentsByBlogId(Long blogId){

        Comment comment = new Comment();
        comment.setBlogId(blogId);
        Example example = new Example(Comment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("blogId", blogId);
        example.setOrderByClause("create_time desc");
        List<Comment> commentList = commentMapper.selectByExample(example);
        return commentList;
    }

    @Transactional
    public void addLike(Long blogId) {
        Integer like = blogDetailMapper.selectLikeByBlogId(blogId);
        if (like==null){
            throw new BlogException(ExceptionEnum.BLOG_NOT_FOUND);
        }
        BlogDetail blogDetail = new BlogDetail();
        blogDetail.setBlogId(blogId);
        blogDetail.setLikeNum(like+1);
        int update = blogDetailMapper.increaceLikeNumById(like+1,blogId);
        if (update==0){
            throw new BlogException(ExceptionEnum.LIKE_ADD_FAIL);
        }
    }

    public int[] queryTagListByCid(Integer defaultCategory) {

        int[] ints = tagMapper.selectTagIdsByCid(defaultCategory);
        return ints;
    }

    public PageResult<BlogDetail> queryBlogDetailListByTagIds(int pageNum, Integer pageSize, String sortBy, int[] tagIds, boolean desc) {
        // 分页
        PageHelper.startPage(pageNum, pageSize);
        // 过滤
        Example example = new Example(BlogDetail.class);
        Example.Criteria criteria = example.createCriteria();
        if(tagIds.length!=0){
            for(int id:tagIds) {
                criteria.orEqualTo("tagId", id).
                        andEqualTo("visible", true);// 剔除加密博客
            }
        }
        // 排序
        if(StringUtils.isNotBlank(sortBy)){
            String orderByClause = sortBy + (desc?" desc":" asc");
            example.setOrderByClause(orderByClause);
        }
        // 去除不需要的字段
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
}
