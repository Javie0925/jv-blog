package com.jvblog.blogadmin.controller;

import com.jvblog.blogadmin.service.CategoryService;
import com.jvblog.blogadmin.service.TagService;
import com.jvblog.service.pojo.Category;
import com.jvblog.service.pojo.Tag;
import com.jvblog.service.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author javie
 * @date 2019/6/12 23:57
 */
@Controller
public class TagController {

    @Autowired
    private TagService tagService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 加载tag页面
     * @param model
     * @return
     */
    @GetMapping(value = {"/tag","/tag.html"})
    public String loadTagPage(Model model){

        // 获取tagList
        List<Tag> tagList = tagService.queryTagList();
        // 添加cname
        for (Tag tag:tagList){
            if (tag.getCid()!=null)
                tag.setCname(categoryService.selectCategoryById(tag.getCid()).getCname());
        }
        // 获取categoryList
        List<Category> categoryList = categoryService.selectCategoryList();
        PageResult<Tag> pageResult = new PageResult<>(tagList.size()+0L, 0L, tagList, 0, 0);
        model.addAttribute("pageResult", pageResult);
        model.addAttribute("categoryList", categoryList);
        return "tag";
    }

    /**
     * 批量删除标签
     * @param paramsMap
     * @return
     */
    @PostMapping("/tag")
    @ResponseBody
    public ResponseEntity<Void> deleteTagByids(@RequestBody Map<String,List<Integer>> paramsMap){

        List<Integer> tagIds = paramsMap.get("tagIds");
        tagService.deleteByTagIds(tagIds);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据id删除单个标签
     * @param tagId
     * @return
     */
    @DeleteMapping("/tag")
    @ResponseBody
    public ResponseEntity<Void> deleteTagById(@RequestParam("tagId") Integer tagId){

        tagService.deleteTagById(tagId);
        return ResponseEntity.ok().build();
    }

    /**
     * 修改标签名
     * @param tag
     * @return
     */
    @PostMapping("/tag/update")
    @ResponseBody
    public ResponseEntity<Void> updateTag(@RequestBody Tag tag){
        tagService.updateTag(tag);
        return ResponseEntity.ok().build();
    }

    /**
     * 添加标签
     * @param
     * @return
     */
    @PostMapping("/tag/add")
    @ResponseBody
    public ResponseEntity<Void> addTag(@RequestBody Map<String,String> paramsMap){
        tagService.addTag(paramsMap.get("tagName"),Integer.parseInt(paramsMap.get("cid")));
        return ResponseEntity.ok().build();
    }
}
