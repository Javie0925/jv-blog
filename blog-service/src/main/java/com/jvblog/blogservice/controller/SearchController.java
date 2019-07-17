package com.jvblog.blogservice.controller;

import com.jvblog.blogservice.service.BlogService;
import com.jvblog.blogservice.service.CategoryService;
import com.jvblog.service.pojo.BlogDetail;
import com.jvblog.service.pojo.BlogpageInfo;
import com.jvblog.service.pojo.Category;
import com.jvblog.service.pojo.Tag;
import com.jvblog.service.utils.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author javie
 * @date 2019/6/17 17:23
 */
@Controller
public class SearchController {

    @Autowired
    private BlogService blogService;
    @Value("${jv.blog.msgPageSize}")
    private Integer msgPageSize;
    @Value("${jv.blog.indexPageSize}")
    private Integer indexPageSize;
    @Value("${jv.blog.searchPageSize}")
    private Integer searchPageSize;
    @Value("${jv.blog.hotBlogPageSize}")
    private Integer hotBlogPageSize;
    @Autowired
    private CategoryService categoryService;

    /**
     * 加载搜索结果
     * @param keyword
     * @param model
     * @return
     */
    @GetMapping("/search/{keyword}")
    public String keywordSearch(@PathVariable("keyword")String keyword,Model model){
        //System.out.println("#################################");
        //System.out.println(keyword);
        // 加载博客列表
        PageResult<BlogDetail> result = blogService.queryBlogDetailListByZone(1, searchPageSize, "create_time", keyword, "title",true);
        // 加载热门博客列表
        PageResult<BlogDetail> hotBlogsPageResult = blogService.queryBlogDetailList(1, hotBlogPageSize, "watch", null, true);
        List<BlogDetail> hotBlogs = hotBlogsPageResult.getItemList();
        // 加载标签云
        List<Tag> tagList = blogService.queryTagList();
        // 加载博客页面信息
        BlogpageInfo blogpageInfo = blogService.queryBlogpageInfo();
        // 添加栏目列表
        List<Category> categoryList = categoryService.selectCategoryListWithContent();
        blogpageInfo.setCategoryList(categoryList);
        // 分装数据
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("hotBlogs", hotBlogs);
        dataMap.put("pageResult", result);
        dataMap.put("tagList", tagList);
        dataMap.put("tagNum",tagList.size());
        dataMap.put("keyword",keyword);
        dataMap.put("blogpageInfo",blogpageInfo);
        model.addAllAttributes(dataMap);
        return "search";
    }

    /**
     * 搜索预检
     * @param keyword
     * @return
     */
    @GetMapping("/search/check/{keyword}")
    @ResponseBody
    public ResponseEntity<Void> searchCheck(@PathVariable("keyword")String keyword){
        if (blogService.searchCheck(keyword)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /**
     * 分页搜索
     * @param pageNum
     * @param keyword
     * @return
     */
    @GetMapping("/search/page/{pageNum}")
    @ResponseBody
    public ResponseEntity<PageResult<BlogDetail>> searchByKeywordAndPage(
            @PathVariable("pageNum")Integer pageNum,
            @RequestParam("keyword")String keyword
    ){

        PageResult<BlogDetail> pageResult =
                blogService.queryBlogDetailListByZone(pageNum, searchPageSize, "create_time", keyword, "title", true);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 加载标签云页面
     * @param model
     * @return
     */
    @GetMapping("/search/tag")
    public String loadTagPage(Model model){
        // 加载标签云
        List<Tag> tagList = blogService.queryTagList();
        // 获取第一个有内容的标签
        int tagId = tagList.get(0).getTagId();
        // 加载博客列表
        PageResult<BlogDetail> pageResult = blogService.queryBlogDetailListByTagId(1, searchPageSize, "create_time", tagId, true);
        // 加载热门博客列表
        PageResult<BlogDetail> hotBlogsPageResult = blogService.queryBlogDetailList(1, hotBlogPageSize, "watch", null, true);
        List<BlogDetail> hotBlogs = hotBlogsPageResult.getItemList();
        // 加载tag信息
        Tag tag = blogService.queryTagById(tagId);
        // 加载博客页面信息
        BlogpageInfo blogpageInfo = blogService.queryBlogpageInfo();
        // 添加栏目列表
        List<Category> categoryList = categoryService.selectCategoryListWithContent();
        blogpageInfo.setCategoryList(categoryList);
        // 封装数据
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("hotBlogs", hotBlogs);
        dataMap.put("pageResult", pageResult);
        dataMap.put("tagList", tagList);
        dataMap.put("tag",tag);
        dataMap.put("blogpageInfo",blogpageInfo);
        model.addAllAttributes(dataMap);

        return "tag";
    }


    /**
     * 点击标签搜索
     * @param tagId
     * @param model
     * @return
     */
    @GetMapping("/search/tag/{tagId}")
    public String searchByTagId(@PathVariable("tagId")Integer tagId, Model model){
        // 加载博客列表
        PageResult<BlogDetail> pageResult = blogService.queryBlogDetailListByTagId(1, searchPageSize, "create_time", tagId, true);
        // 加载热门博客列表
        PageResult<BlogDetail> hotBlogsPageResult = blogService.queryBlogDetailList(1, hotBlogPageSize, "watch", null, true);
        List<BlogDetail> hotBlogs = hotBlogsPageResult.getItemList();
        // 加载标签云
        List<Tag> tagList = blogService.queryTagList();
        // 加载tag信息
        Tag tag = blogService.queryTagById(tagId);
        // 加载博客页面信息
        BlogpageInfo blogpageInfo = blogService.queryBlogpageInfo();
        // 添加栏目列表
        List<Category> categoryList = categoryService.selectCategoryListWithContent();
        blogpageInfo.setCategoryList(categoryList);
        // 封装数据
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("hotBlogs", hotBlogs);
        dataMap.put("pageResult", pageResult);
        dataMap.put("tagList", tagList);
        dataMap.put("tag",tag);
        dataMap.put("blogpageInfo",blogpageInfo);
        model.addAllAttributes(dataMap);
        return "tag";
    }

    /**
     * 标签搜索分页
     * @param pageNum
     * @param tagId
     * @return
     */
    @GetMapping("/search/tag/page/{pageNum}")
    public ResponseEntity<PageResult<BlogDetail>> queryBlogByTageAndPage(
            @PathVariable("pageNum")Integer pageNum,
            @RequestParam("tagId")Integer tagId
    ){
        PageResult<BlogDetail> pageResult = blogService.queryBlogDetailListByTagId(pageNum, searchPageSize, "create_time", tagId, true);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/tag/{tagId}/reload")
    public ResponseEntity<PageResult<BlogDetail>> reloadBlogListByTagId(@PathVariable("tagId")Integer tagId){
        PageResult<BlogDetail> pageResult = blogService.queryBlogDetailListByTagId(1, searchPageSize, "create_time", tagId, true);
        return ResponseEntity.ok(pageResult);
    }
}
