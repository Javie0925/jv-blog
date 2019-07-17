package com.jvblog.blogservice.controller;

import com.jvblog.blogservice.service.CommentService;
import com.jvblog.service.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author javie
 * @date 2019/6/27 23:46
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/comment")
    @ResponseBody
    public ResponseEntity<Void> createComment(@RequestBody Comment comment, HttpServletRequest request){

        commentService.createdComment(comment,request.getHeader("X-Real-Ip"));
        return ResponseEntity.ok().build();
    }
}
