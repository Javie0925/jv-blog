package com.jvblog.blogadmin.controller;

import com.jvblog.blogadmin.service.SlidePicService;
import com.jvblog.service.pojo.SlidePic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author javie
 * @date 2019/6/14 11:32
 */
@Controller
public class SlidePicController {

    @Autowired
    private SlidePicService slidePicService;

    /**
     * 加载滚动图管理页面
     * @param model
     * @return
     */
    @GetMapping({"slide","slide.html"})
    public String loadSlidePicPage(Model model){

        List<SlidePic> slidePicList = slidePicService.getSlidePicList();
        model.addAttribute("slidePicList",slidePicList);
        return "slide.html";
    }

    /**
     * 修改滚动图状态
     * @param paramsMap
     * @return
     */
    @PutMapping("/slide/state")
    @ResponseBody
    public ResponseEntity<Void> alterSlidePicState(@RequestBody Map<String,Integer> paramsMap){
        slidePicService.alterSlidePicState(paramsMap.get("id"));
        return ResponseEntity.ok().build();
    }

    /**
     * 修改滚动图信息
     * @param slidePic
     * @return
     */
    @PutMapping("/slide")
    @ResponseBody
    public ResponseEntity<Void> updateSlidePic(@RequestBody SlidePic slidePic){

        slidePicService.updateSlidePic(slidePic);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/slide")
    @ResponseBody
    public ResponseEntity<Void> addSlidePic(@RequestBody SlidePic slidePic){
        slidePicService.addSlidePic(slidePic);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/slide/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteById(@PathVariable("id")Integer id){
        slidePicService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
