package com.jvblog.blogservice.controller;

import com.jvblog.blogservice.service.BlogService;
import com.jvblog.blogservice.service.CategoryService;
import com.jvblog.service.mapper.BlogDetailMapper;
import com.jvblog.service.mapper.TagMapper;
import com.jvblog.service.pojo.BlogDetail;
import com.jvblog.service.pojo.BlogpageInfo;
import com.jvblog.service.pojo.Category;
import com.jvblog.service.pojo.Tag;
import com.jvblog.service.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author javie
 * @date 2019/7/5 13:35
 */
@Controller
public class TimeController {

    @Autowired
    private BlogService blogService;
    @Value("${jv.blog.hotBlogPageSize}")
    private Integer hotBlogPageSize;
    @Autowired
    private CategoryService categoryService;

    @GetMapping({"/time.html","time"})
    public String loadTimePage(Model model){
        // 博客列表
        PageResult<BlogDetail> pageResult = blogService.queryBlogDetailList(1, 1000, "create_time", null, true);
        // pageInfo
        BlogpageInfo blogpageInfo = blogService.queryBlogpageInfo();
        // 添加栏目列表
        List<Category> categoryList = categoryService.selectCategoryListWithContent();
        blogpageInfo.setCategoryList(categoryList);
        // tagList
        List<Tag> tagList = blogService.queryTagList();
        // hotBlogs
        PageResult<BlogDetail> hotBlogsPageResult = blogService.queryBlogDetailList(1, hotBlogPageSize, "watch", null, true);
        List<BlogDetail> hotBlogs = hotBlogsPageResult.getItemList();

        model.addAttribute("pageResult", pageResult);
        model.addAttribute("blogpageInfo", blogpageInfo);
        model.addAttribute("tagList", tagList);
        model.addAttribute("hotBlogs", hotBlogs);
        return "time";
    }
}
