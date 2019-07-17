package com.jvblog.blogadmin.controller;

import com.jvblog.blogadmin.service.PageInfoService;
import com.jvblog.service.pojo.BlogpageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author javie
 * @date 2019/6/13 13:21
 */
@Controller
public class PageInfoController {

    @Autowired
    private PageInfoService pageInfoService;

    /**
     * 加载页面信息页
     * @param model
     * @return
     */
    @GetMapping(value = {"page-info","page-info.html"})
    public String loadPageInfoPage(Model model){

        // 获取blogpageInfo
        BlogpageInfo blogpageInfo = pageInfoService.getBlogpageInfo();
        model.addAttribute("blogpageInfo", blogpageInfo);
        // 返回视图
        return "page-info";
    }

    /**
     * 添加链接
     * @param paramsMap
     * @return
     */
    @PostMapping("/pageinfo/link")
    @ResponseBody
    public ResponseEntity<Void> addLink(@RequestBody Map<String,String> paramsMap){

        pageInfoService.addLink(paramsMap.get("name"),paramsMap.get("ip"));

        return ResponseEntity.ok().build();
    }

    /**
     * 删除链接
     * @param name
     * @return
     */
    @DeleteMapping("/pageinfo/link")
    @ResponseBody
    public ResponseEntity<Void> deleteLink(@RequestParam("name")String name){

        pageInfoService.deleteLinkByName(name);

        return ResponseEntity.ok().build();
    }

    /**
     * 修改页头信息
     * @param paramsMap
     * @return
     */
    @PostMapping("/pageinfo/headermsg")
    @ResponseBody
    public ResponseEntity<Void> updateHeaderMsg(@RequestBody Map<String,String> paramsMap){

        pageInfoService.updateHeaderMsg(paramsMap.get("name"),paramsMap.get("text"));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/pageinfo/update")
    @ResponseBody
    public ResponseEntity<Void> updateBlogpageInfo(@RequestBody BlogpageInfo blogpageInfo){

        pageInfoService.updateBlogpageInfo(blogpageInfo);
        return ResponseEntity.ok().build();
    }
}
