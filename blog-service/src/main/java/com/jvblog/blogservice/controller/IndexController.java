package com.jvblog.blogservice.controller;

import com.jvblog.blogservice.service.BlogService;
import com.jvblog.blogservice.service.CategoryService;
import com.jvblog.blogservice.service.RedisSevice;
import com.jvblog.service.pojo.*;
import com.jvblog.service.utils.AddressUtils;
import com.jvblog.service.utils.IpUtils;
import com.jvblog.service.utils.PageResult;
import com.jvblog.service.vo.BlogVo;
import com.jvblog.service.vo.MsgVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author javie
 * @date 2019/6/2 19:38
 */
@Slf4j
@Controller
public class IndexController {

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
    @Autowired
    private RedisSevice redisSevice;

    /**
     * 加载主页信息
     * @param model
     * @return
     */
    @GetMapping(value = {"/index","index.html","/"})
    public String index(Model model){
        // 去redis中查找数据
        /*try {
            Map<String, Object> dataMap = redisSevice.getIndexDataFromRedis();
            // 判空
            if (dataMap!=null&&dataMap.size()==6){
                // 判断有值，加入hotBlogs(因为热门博客数据会随时更新，所以要从数据库中取)
                PageResult<BlogDetail> hotBlogsPageResult = blogService.queryBlogDetailList(1, hotBlogPageSize, "watch", null, true);
                List<BlogDetail> hotBlogs = hotBlogsPageResult.getItemList();
                dataMap.put("hotBlogs", hotBlogs);
                model.addAllAttributes(dataMap);
                // 返回视图
                return "index";
            }
        }catch(Exception e){
            log.error("############################INDEX-DATA读取数据失败############################");
        }*/
        // 加载博客列表
        PageResult<BlogDetail> result = blogService.queryBlogDetailList(1, indexPageSize, "create_time", null, true);
        // 加载热门博客列表
        PageResult<BlogDetail> hotBlogsPageResult = blogService.queryBlogDetailList(1, hotBlogPageSize, "watch", null, true);
        List<BlogDetail> hotBlogs = hotBlogsPageResult.getItemList();
        // 加载标签云
        List<Tag> tagList = blogService.queryTagList();
        // 加载博客页面信息
        BlogpageInfo blogpageInfo = blogService.queryBlogpageInfo();
        // 添加栏目信息
        List<Category> categoryList = categoryService.selectCategoryListWithContent();
        blogpageInfo.setCategoryList(categoryList);
        // 加载我的信息
        AboutMe aboutMe = blogService.queryAboutMe();
        // 加载slidePicList信息
        List<SlidePic> slidePicList = blogService.querySlidePicList();
        // 封装数据
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("hotBlogs", hotBlogs);
        dataMap.put("pageResult", result);
        dataMap.put("tagList", tagList);
        dataMap.put("aboutMe",aboutMe);
        dataMap.put("blogpageInfo",blogpageInfo);
        dataMap.put("slidePicList", slidePicList);
        model.addAllAttributes(dataMap);
        /*// 将数据存入redis中
        try {
            redisSevice.insertIndexDataToRedis(dataMap);
        }catch(Exception e){
            log.error("############################INDEX-DATA保存到Redis失败############################");
        }*/
        return "index";
    }

    /**
     * 分类排行
     * @return
     */
    @GetMapping("/index/{sortBy}")
    @ResponseBody
    public ResponseEntity<PageResult<BlogDetail>> loadBlogListBySort(@PathVariable("sortBy")String sortBy){
        PageResult<BlogDetail> pageResult = blogService.queryBlogDetailList(1, indexPageSize, sortBy, null, true);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 主页分页查询博客
     * @param pageNum
     * @param pageSize
     * @param sortBy
     * @return
     */
    @GetMapping("/page/{pageNum}")
    @ResponseBody
    public ResponseEntity<PageResult> loadBlogListByPage(
            @PathVariable("pageNum")int pageNum,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10")int pageSize,
            @RequestParam(value = "sortBy",required = false,defaultValue = "create_time")String sortBy){
        // 加载博客列表
        PageResult<BlogDetail> pageResult = blogService.queryBlogDetailList(pageNum, pageSize, sortBy, null, true);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 加载博文详情页
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blog/{id}")
    public String loadBlogDetail(@PathVariable("id")Long id,Model model){
        // blogVo
        BlogVo blogVo = blogService.queryBlogById(id);
        Map<String,Object> dataMap = new HashMap<>();
        // 加载博客页面信息，并增加访问次数
        BlogpageInfo blogpageInfo = blogService.queryBlogpageInfo();
        // 添加栏目信息
        List<Category> categoryList = categoryService.selectCategoryListWithContent();
        blogpageInfo.setCategoryList(categoryList);
        // 封装数据
        dataMap.put("blogVo", blogVo);
        dataMap.put("blogpageInfo", blogpageInfo);
        model.addAllAttributes(dataMap);
        return "info";
    }

    /**
     * 添加博客点赞数量
     * @param blogId
     * @return
     */
    @PostMapping("/blog/like/{blogId}")
    public ResponseEntity<Void> addLike(@PathVariable("blogId") Long blogId){
        if (blogId==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        blogService.addLike(blogId);
        return ResponseEntity.ok().build();
    }
}
