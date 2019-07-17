package com.jvblog.blogadmin.controller;

import com.jvblog.blogadmin.service.ImageUploadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author javie
 * @date 2019/6/11 13:57
 */
@Controller
@RequestMapping("/ueditor")
public class UeditorController {

    @Autowired
    private ImageUploadService imageUploadService;

    /**
     * 页面请求获取config.json
     * @return
     */
    @GetMapping("/config")
    public String getConfig(){
//        return "redirect:/config.json";
        return "config.json";
    }

    /**
     * 图片上传
     * @param upfile
     * @return
     */
    @PostMapping("/config")
    @ResponseBody
    public Map<String, Object> uploadImage(MultipartFile upfile) {
        HashMap<String, Object> params = new HashMap<>();
        try {
            System.out.println("#####################################################文件上传");
            FileInputStream fileInputStream = (FileInputStream) upfile.getInputStream();
            String imgUrl = imageUploadService.uploadQNImg(fileInputStream, UUID.randomUUID().toString());
            if (StringUtils.isEmpty(imgUrl)){
                params.put("state", "ERROR");
                return params;
            }
            params.put("state", "SUCCESS");
            params.put("url", imgUrl);
            params.put("size", upfile.getSize());
            params.put("original", upfile.getName());
            params.put("type", upfile.getContentType());
        } catch (Exception e) {
            params.put("state", "ERROR");
            e.printStackTrace();
        }
            return params;
    }
}
