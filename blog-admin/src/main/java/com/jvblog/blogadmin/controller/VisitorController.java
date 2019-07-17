package com.jvblog.blogadmin.controller;

import com.jvblog.blogadmin.service.VisitorService;
import com.jvblog.service.pojo.BlogDetail;
import com.jvblog.service.pojo.Visitor;
import com.jvblog.service.utils.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author javie
 * @date 2019/6/14 21:19
 */
@Controller
public class VisitorController {

    @Autowired
    private VisitorService visitorService;

    @GetMapping({"/visitor","visitor.html"})
    public String loadVisitorPage(Model model){

        PageResult<Visitor> pageResult = visitorService.queryVisitorsByPage(1,10,"id",true);
        model.addAttribute("pageResult", pageResult);
        return "visitor";
    }

    /**
     * 删除单个访客信息
     * @param id
     * @return
     */
    @PostMapping("/visitor/delete")
    @ResponseBody
    public ResponseEntity<Void> deleteVisitorById(@RequestParam("id")Long id){
        visitorService.deleteVisitorById(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @param sortBy
     * @param desc
     * @return
     */
    @GetMapping("/visitor/page")
    @ResponseBody
    public ResponseEntity<PageResult<Visitor>> axiosLoadBlogList(
            @RequestParam(value = "pageNum",required = false,defaultValue = "1")Integer pageNum,
            @RequestParam(value = "pageSize",required = false,defaultValue = "10")Integer pageSize,
            @RequestParam(value = "sortBy",required = false,defaultValue = "id")String sortBy,
            @RequestParam(value = "desc",required = false,defaultValue = "true")Boolean desc
    ){
        PageResult<Visitor> result = visitorService.queryVisitorsByPage(pageNum, pageSize, sortBy, desc);
        return ResponseEntity.ok(result);
    }

    /**
     * 批量删除访客信息
     * @param
     * @return
     */
    @PostMapping("/visitor")
    @ResponseBody
    public ResponseEntity<Void> deleteVisitorByIds(@RequestBody Map<String,List<Long>> paramsMap){
        System.out.println("##########################################"+paramsMap);
        visitorService.deleteVisitorByIds(paramsMap.get("ids"));

        return ResponseEntity.ok().build();
    }
}
