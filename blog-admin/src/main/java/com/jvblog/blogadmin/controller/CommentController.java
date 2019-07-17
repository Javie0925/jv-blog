package com.jvblog.blogadmin.controller;

import com.jvblog.blogadmin.service.BlogService;
import com.jvblog.blogadmin.service.CommentService;
import com.jvblog.service.pojo.BlogDetail;
import com.jvblog.service.pojo.Comment;
import com.jvblog.service.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author javie
 * @date 2019/7/1 12:04
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogService blogService;

    /**
     * 加载评论页面
     * @param model
     * @return
     */
    @GetMapping({"/comment","/comment.html"})
    public String loadCommentPage(Model model){
        PageResult<Comment> pageResult = commentService.queryCommentsByKeyAndPage(1, 10, "create_time", null, true);
        List<BlogDetail> blogList = blogService.getBlogIdAndTitleByBlogId();
        model.addAttribute("pageResult", pageResult);
        model.addAttribute("blogList", blogList);
        return "comment";
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param blogId
     * @param sortBy
     * @param desc
     * @return
     */
    @GetMapping("/comment/page")
    @ResponseBody
    public ResponseEntity<PageResult<Comment>> axiosQueryCommentByPage(
            @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
            @RequestParam(value = "blogId",required = false,defaultValue = "0")Long blogId,
            @RequestParam(value = "sortBy",required = false,defaultValue = "create_time")String sortBy,
            @RequestParam(value = "desc",required = false,defaultValue = "true")Boolean desc
    ){
        PageResult<Comment> pageResult = commentService.queryCommentsByKeyAndPage(pageNum, pageSize, sortBy, blogId, desc);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 删除单个
     * @param id
     * @return
     */
    @DeleteMapping("/comment")
    public ResponseEntity<Void> deleteById(@RequestParam("commentId") Integer id){

        commentService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("/comment")
    public ResponseEntity<Void> deleteByIds(@RequestBody List<Integer> ids){
        commentService.deleteByIds(ids);
        return ResponseEntity.ok().build();
    }

}
