package com.jvblog.blogservice.controller;

import com.jvblog.blogservice.service.BlogService;
import com.jvblog.blogservice.service.CategoryService;
import com.jvblog.service.pojo.AboutMe;
import com.jvblog.service.pojo.BlogpageInfo;
import com.jvblog.service.pojo.Category;
import com.jvblog.service.pojo.Message;
import com.jvblog.service.utils.AddressUtils;
import com.jvblog.service.utils.PageResult;
import com.jvblog.service.vo.MsgVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * @date 2019/6/17 17:20
 */
@Controller
public class MessageController {

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
     * 加载留言页
     * @param model
     * @return
     */
    @GetMapping("/message")
    public String loadMessagePage(Model model){

        PageResult<Message> pageResult = blogService.queryMessageByPage(1,msgPageSize);
        // 分组
        List<Message> resultList = pageResult.getItemList();
        List<Message> msgList1 = new ArrayList<>();
        List<Message> msgList2 = new ArrayList<>();
        List<Message> msgList3 = new ArrayList<>();
        for(int i = 0;i<resultList.size();i++) {
            if (i % 3 == 0) {
                msgList1.add(resultList.get(i));
            }
            if (i % 3 == 1) {
                msgList2.add(resultList.get(i));
            }
            if (i % 3 == 2) {
                msgList3.add(resultList.get(i));
            }
        }
        // 加载博客页面信息
        BlogpageInfo blogpageInfo = blogService.queryBlogpageInfo();
        // 添加栏目信息
        List<Category> categoryList = categoryService.selectCategoryListWithContent();
        blogpageInfo.setCategoryList(categoryList);
        // 封装模型
        model.addAttribute("pageResult", pageResult);
        model.addAttribute("msgList1", msgList1);
        model.addAttribute("msgList2", msgList2);
        model.addAttribute("msgList3", msgList3);
        model.addAttribute("blogpageInfo", blogpageInfo);
        return "message";
    }

    /**
     * 留言分页查询
     * @param pageNum
     * @return
     */
    @GetMapping("/message/{pageNum}")
    @ResponseBody
    public ResponseEntity<Map<String,Object>> queryMessageByPage(@PathVariable("pageNum") Integer pageNum){
        PageResult<Message> pageResult = blogService.queryMessageByPage(pageNum, msgPageSize);
        // 分组
        List<Message> resultList = pageResult.getItemList();
        List<Message> msgList1 = new ArrayList<>();
        List<Message> msgList2 = new ArrayList<>();
        List<Message> msgList3 = new ArrayList<>();
        for(int i = 0;i<resultList.size();i++) {
            if (i % 3 == 0) {
                msgList1.add(resultList.get(i));
            }
            if (i % 3 == 1) {
                msgList2.add(resultList.get(i));
            }
            if (i % 3 == 2) {
                msgList3.add(resultList.get(i));
            }
        }
        // 封装模型
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("pageResult", pageResult);
        dataMap.put("msgList1", msgList1);
        dataMap.put("msgList2", msgList2);
        dataMap.put("msgList3", msgList3);
        return ResponseEntity.ok(dataMap);
    }

    /**
     * 创建留言信息
     * @param msgVo
     * @param request
     * @return
     */
    @PostMapping("/message")
    public ResponseEntity<Void> createMessage(@RequestBody MsgVo msgVo, HttpServletRequest request){

        // 获取访客信息
        // String ip = IpUtils.getIpFromRequest(request);
        String ip = request.getHeader("X-Real-Ip");
        String address = null;
        try {
            address = AddressUtils.getAddresses(ip);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        blogService.createMessage(msgVo,ip,address);
        return ResponseEntity.ok().build();
    }

    /**
     * 加载关于我页面
     * @param model
     * @return
     */
    @GetMapping("/about")
    public String loadAboutPage(Model model){
        // 加载blogpageInfo
        BlogpageInfo blogpageInfo = blogService.queryBlogpageInfo();
        // 添加栏目信息
        List<Category> categoryList = categoryService.selectCategoryListWithContent();
        blogpageInfo.setCategoryList(categoryList);
        // 加载aboutMe信息
        AboutMe aboutMe = blogService.queryAboutMe();
        model.addAttribute("aboutMe", aboutMe);
        model.addAttribute("blogpageInfo", blogpageInfo);
        return "about";
    }
}
