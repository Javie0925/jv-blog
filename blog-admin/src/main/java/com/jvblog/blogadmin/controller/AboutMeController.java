package com.jvblog.blogadmin.controller;

import com.jvblog.blogadmin.service.AboutMeService;
import com.jvblog.service.pojo.AboutMe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author javie
 * @date 2019/6/16 1:00
 */
@Controller
public class AboutMeController {

    @Autowired
    private AboutMeService aboutMeService;

    /**
     * 加载me.html页面
     * @param model
     * @return
     */
    @GetMapping({"me.html","me"})
    public String loadAboutMePage(Model model){

        AboutMe aboutMe = aboutMeService.getAboutMe();
        aboutMe.setSummary(null);
        aboutMe.setHtml(null);
        model.addAttribute("aboutMe", aboutMe);
        return "me";
    }

    /**
     * 修改基本信息
     * @param aboutMe
     * @return
     */
    @PutMapping("/me")
    public ResponseEntity<Void> updateBasicInfo(@RequestBody AboutMe aboutMe){

        aboutMeService.updateBasicInfo(aboutMe);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据名字删除联系方式
     * @param name
     * @return
     */
    @DeleteMapping("/me/contact")
    public ResponseEntity<Void> delete(@RequestParam("name")String name){

        aboutMeService.deleteByName(name);
        return ResponseEntity.ok().build();
    }

    /**
     * 添加联系方式
     * @param contactMap
     * @return
     */
    @PostMapping("/me/contact")
    public ResponseEntity<Void> addContact(@RequestBody Map<String,String> contactMap){
        aboutMeService.addContact(contactMap);
        return ResponseEntity.ok().build();
    }

    /**
     * 加载内容编辑页面
     * @param model
     * @return
     */
    @GetMapping({"/me-text.html","/me-text"})
    public String loadMeTextPage(Model model){

        AboutMe aboutMe = aboutMeService.getAboutMe();
        aboutMe.setContact(null);
        aboutMe.setAvator(null);
        aboutMe.setJob(null);
        aboutMe.setNickname(null);
        model.addAttribute("aboutMe", aboutMe);
        return "me-text";
    }
    @PutMapping("/me/text")
    public ResponseEntity<Void> updateAboutMeText(@RequestBody AboutMe aboutMe){
        aboutMeService.updateText(aboutMe);
        return ResponseEntity.ok().build();
    }
}
