package com.jvblog.blogadmin.controller;

import com.jvblog.blogadmin.pojo.IndexVo;
import com.jvblog.blogadmin.service.BlogService;
import com.jvblog.blogadmin.service.CategoryService;
import com.jvblog.blogadmin.service.IndexService;
import com.jvblog.blogadmin.service.TagService;
import com.jvblog.service.pojo.BlogDetail;
import com.jvblog.service.pojo.Category;
import com.jvblog.service.pojo.Tag;
import com.jvblog.service.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author javie
 * @date 2019/6/9 3:04
 */
@Controller
public class AdminController {
    @Autowired
    private BlogService blogService;
    @Autowired
    private IndexService indexService;
    @Autowired
    private TagService tagService;

    /*@RequestMapping("/{page}")
    public String loadPage(@PathVariable("page")String page){
        return page;
    }*/

    @GetMapping({"/index.html","/index"})
    public String loadIndex(Model model){

        // 获取所需信息
        IndexVo indexVo = indexService.queryIndexData();
        model.addAttribute("indexVo", indexVo);
        return "index";
    }

    /**
     * 加载博客列表页面
     * @param model
     * @return
     */
    @GetMapping({"/article","article.html"})
    public String loadBlogList(Model model){
        //博客列表
        PageResult<BlogDetail> result = blogService.queryBlogDetailsByKeyAndPage(1, 10, "create_time", 0,"", true);
        //标签列表
        List<Tag> tagList = tagService.queryTagList();
        //去除空标签
        for(int i=0;i<tagList.size();){
            Tag tag = tagList.get(i);
            if (tag.getBlogCount()==0){
                tagList.remove(tag);
                continue;
            }
            i++;
        }
        model.addAttribute("tagList", tagList);
        model.addAttribute("pageResult", result);
        return "article";
    }

    /**
     * 异步加载文章列表
     * @param pageNum
     * @param pageSize
     * @param key
     * @param sortBy
     * @param desc
     * @return
     */
    @GetMapping("/article/page")
    @ResponseBody
    public ResponseEntity<PageResult<BlogDetail>> axiosLoadBlogList(
            @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
            @RequestParam(value = "key",required = false,defaultValue = "")String key,
            @RequestParam(value = "tagId",required = false,defaultValue = "0") Integer tagId,
            @RequestParam(value = "sortBy",required = false,defaultValue = "create_time")String sortBy,
            @RequestParam(value = "desc",required = false,defaultValue = "true")Boolean desc
    ){
        PageResult<BlogDetail> result = blogService.queryBlogDetailsByKeyAndPage(pageNum, pageSize,sortBy,tagId, key, desc);
        return ResponseEntity.ok(result);
    }

    /**
     * 根据id删除单个blog
     * @param blogId
     * @return
     */
    @DeleteMapping("/article/delete")
    @ResponseBody
    public ResponseEntity<Void> deleteBlogById(@RequestParam("blogId")Long blogId){
        blogService.deleteBlogById(blogId);
        return ResponseEntity.ok().build();
    }

    /**
     * 批量删除blog
     * @param map
     * @return
     */
    @PostMapping("/article")
    @ResponseBody
    public ResponseEntity<Void> deleteBlogByIds(@RequestBody Map<String,List<Long>> map){
        List<Long> blogIds = map.get("blogIds");
        if (blogIds==null||blogIds.size()==0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        blogService.deleteBlogByIds(blogIds);
        return ResponseEntity.ok().build();
    }

    /**
     * 加载博客修改页面
     * @param blogId
     * @return
     */
    @GetMapping("/update-article/{blogId}")
    public String loadUpdatePage(@PathVariable("blogId") Long blogId,Model model){

        // 获得blog
        BlogDetail blogDetail = blogService.queryBlogDetailById(blogId);
        // 获取tagList
        List<Tag> tagList = blogService.queryTagList();
        model.addAttribute("blogDetail", blogDetail);
        model.addAttribute("tagList", tagList);
        return "update-article";
    }

    /**
     * 更新修改blog
     * @param
     * @return
     */
    @PostMapping("/article/update")
    @ResponseBody
    public ResponseEntity<Void> updateArticle(@RequestBody BlogDetail blogDetail){

        blogService.updateBlog(blogDetail);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据id获取博客title
     * @param blogId
     * @return
     */
    @GetMapping("/blog/{blogId}")
    @ResponseBody
    public ResponseEntity<BlogDetail> queryBlogDetailById(@PathVariable("blogId")Long blogId){
        // 获取blogDetail
        BlogDetail blogDetail = blogService.queryBlogDetailById(blogId);
        // 创建新blogDetail
        BlogDetail blogDetail_ = new BlogDetail();
        // 设置title
        blogDetail_.setTitle(blogDetail.getTitle());
        // 返回
        return ResponseEntity.ok(blogDetail_);
    }

    /**
     * 修改博客状态方法
     * @param paramsMap
     * @return
     */
    @PutMapping("/article/state")
    @ResponseBody
    public ResponseEntity<Void> updateStateById(@RequestBody Map<String,Long> paramsMap){

        blogService.updateBlogStateById(paramsMap.get("blogId"));

        return ResponseEntity.ok().build();
    }

    /**
     * 加载修改博客页面
     * @param model
     * @return
     */
    @GetMapping({"add-article","add-article.html"})
    public String loadAddArticlePage(Model model){
        // 获得blog
        BlogDetail blogDetail = new BlogDetail();
        // 获取tagList
        List<Tag> tagList = blogService.queryTagList();
        model.addAttribute("blogDetail", blogDetail);
        model.addAttribute("tagList", tagList);

        return "add-article";
    }

    /**
     * 添加博客
     * @param blogDetail
     * @return
     */
    @PostMapping("/article/add")
    public ResponseEntity addArticle(@RequestBody BlogDetail blogDetail){

        blogService.addArticle(blogDetail);
        return ResponseEntity.ok().build();
    }

}
