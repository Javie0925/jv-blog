package com.jvblog.blogadmin.controller;

import com.jvblog.blogadmin.service.CategoryService;
import com.jvblog.blogadmin.service.TagService;
import com.jvblog.service.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @author javie
 * @date 2019/7/13 2:11
 */
@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;

    @GetMapping({"/category.html","category"})
    public String loadCategoryPage(Model model){

        List<Category> categoryList = categoryService.selectCategoryList();
        // 添加标签数量
        for(Category category: categoryList){
            category.setTagCount(tagService.selectCountByCid(category.getCid()));
        }
        model.addAttribute("categoryList", categoryList);
        return "category";
    }

    /**
     * 删除单个标签方法
     * @param cid
     * @return
     */
    @DeleteMapping("/category")
    public ResponseEntity<Void> deleteByCide(@RequestParam("cid") Integer cid){
        categoryService.deleteByCide(cid);
        return ResponseEntity.ok().build();
    }

    /**
     * 修改栏目名称
     * @param category
     * @return
     */
    @PostMapping("/category/update")
    public ResponseEntity<Void> update(@RequestBody Category category){
        categoryService.update(category);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/category/add")
    public ResponseEntity<Void> addCategory(@RequestBody HashMap<String,String> map){
        categoryService.addCategory(map.get("cname"));
        return ResponseEntity.ok().build();
    }
}
