package com.jvblog.blogadmin.controller;

import com.jvblog.blogadmin.service.MessageService;
import com.jvblog.service.pojo.Message;
import com.jvblog.service.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author javie
 * @date 2019/6/9 21:05
 */
@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    /**
     * 加载留言管理页面
     * @param model
     * @return
     */
    @GetMapping(value = {"/message","/message.html"})
    public String loadMessagePage(Model model){

        PageResult<Message> pageResult = messageService.queryMessageByPage(1,10);
        model.addAttribute("pageResult", pageResult);
        return "message";
    }

    /**
     * 异步加载分页信息
     * @param pageNum
     * @param pageSize
     * @param key
     * @param sortBy
     * @param desc
     * @return
     */
    @GetMapping("/message/page")
    @ResponseBody
    public ResponseEntity<PageResult<Message>> axiosQueryMsgByPage(
            @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
            @RequestParam(value = "key",required = false,defaultValue = "")String key,
            @RequestParam(value = "sortBy",required = false,defaultValue = "create_time")String sortBy,
            @RequestParam(value = "desc",required = false,defaultValue = "true")Boolean desc
    ){
        PageResult<Message> pageResult = messageService.queryMsgsByKeyAndPage(pageNum, pageSize, sortBy, key, desc);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * 根据id删除留言
     * @param msgId
     * @return
     */
    @DeleteMapping("/message")
    @ResponseBody
    public ResponseEntity<Void> deleteMesByIds(@RequestParam("msgId")Long msgId ){

        List<Long> ids = new ArrayList<Long>();
        ids.add(msgId);
        messageService.deleteMsgByIds(ids);

        return ResponseEntity.ok().build();
    }

    /**
     * 批量删除
     * @param map
     * @return
     */
    @PostMapping("/message")
    @ResponseBody
    public ResponseEntity<Void> deleteMsgByIds(@RequestBody Map<String,List<Long>> map){

        messageService.deleteMsgByIds(map.get("msgIds"));
        return ResponseEntity.ok().build();
    }
}
