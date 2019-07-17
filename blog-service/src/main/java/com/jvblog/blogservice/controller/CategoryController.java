package com.jvblog.blogservice.controller;

import com.jvblog.blogservice.service.BlogService;
import com.jvblog.blogservice.service.CategoryService;
import com.jvblog.service.pojo.BlogDetail;
import com.jvblog.service.pojo.BlogpageInfo;
import com.jvblog.service.pojo.Category;
import com.jvblog.service.pojo.Tag;
import com.jvblog.service.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author javie
 * @date 2019/7/13 0:07
 */
@Controller
public class CategoryController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private CategoryService categoryService;
    @Value("${jv.blog.categoryPageSize}")
    private Integer categoryPageSize;
    @Value("${jv.blog.hotBlogPageSize}")
    private Integer hotBlogPageSize;

    @GetMapping({"/category","category.html"})
    public String loadCategoryPage(Model model){
        // 获取所有有内容的栏目
        List<Category> categoryList = categoryService.selectCategoryListWithContent();
        // 获取默认分类信息,栏目列表的第一个栏目作为默认栏目
        Category currentCategory = categoryService.selectByCid(categoryList.get(0).getCid());
        // 获取taglist
        int[] tagIds = blogService.queryTagListByCid(categoryList.get(0).getCid());
        // 加载博客列表
        PageResult<BlogDetail> pageResult = blogService.queryBlogDetailListByTagIds(1, categoryPageSize, "create_time", tagIds, true);
        // 加载热门博客列表
        PageResult<BlogDetail> hotBlogsPageResult = blogService.queryBlogDetailList(1, hotBlogPageSize, "watch", null, true);
        List<BlogDetail> hotBlogs = hotBlogsPageResult.getItemList();
        // 加载标签云
        List<Tag> tagList = blogService.queryTagList();
        // 加载博客页面信息
        BlogpageInfo blogpageInfo = blogService.queryBlogpageInfo();
        // 添加栏目列表
        blogpageInfo.setCategoryList(categoryList);
        // 封装数据
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("hotBlogs", hotBlogs);
        dataMap.put("pageResult", pageResult);
        dataMap.put("tagList", tagList);
        dataMap.put("blogpageInfo",blogpageInfo);
        dataMap.put("currentCategory",currentCategory);
        model.addAllAttributes(dataMap);

        return "category";
    }

    @GetMapping({"/category/{cid}"})
    public String loadCategoryPageByCid(Model model,@PathVariable("cid")Integer cid){
        // 获取分类信息
        Category currentCategory = categoryService.selectByCid(cid);
        // 获取taglist
        int[] tagIds = blogService.queryTagListByCid(cid);
        // 加载博客列表
        PageResult<BlogDetail> pageResult = blogService.queryBlogDetailListByTagIds(1, categoryPageSize, "create_time", tagIds, true);
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
        // 封装数据
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("hotBlogs", hotBlogs);
        dataMap.put("pageResult", pageResult);
        dataMap.put("tagList", tagList);
        dataMap.put("blogpageInfo",blogpageInfo);
        dataMap.put("currentCategory",currentCategory);
        model.addAllAttributes(dataMap);

        return "category";
    }

    /**
     * 分页
     * @param pageNum
     * @param cid
     * @return
     */
    @GetMapping("/category/page/")
    public ResponseEntity<PageResult<BlogDetail>> queryBlogByTageAndPage(
            @RequestParam("pageNum")Integer pageNum,
            @RequestParam("currentCategory")Integer cid
    ){
        int[] tagIds = blogService.queryTagListByCid(cid);
        PageResult<BlogDetail> pageResult = blogService.queryBlogDetailListByTagIds(pageNum, categoryPageSize, "create_time", tagIds, true);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 加载指定标签内容
     * @param cid
     * @return
     */
    @GetMapping("/category/{cid}/reload")
    public ResponseEntity<PageResult<BlogDetail>> getCategoryList(@PathVariable("cid")Integer cid){
        int[] tagIds = blogService.queryTagListByCid(cid);
        PageResult<BlogDetail> pageResult = blogService.queryBlogDetailListByTagIds(1, categoryPageSize, "create_time", tagIds, true);
        return ResponseEntity.ok(pageResult);
    }
}
