package com.jvblog.blogservice.service;

import com.jvblog.service.enums.ExceptionEnum;
import com.jvblog.service.exception.BlogException;
import com.jvblog.service.mapper.CategoryMapper;
import com.jvblog.service.mapper.TagMapper;
import com.jvblog.service.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author javie
 * @date 2019/7/14 2:28
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private TagMapper tagMapper;

    public List<Category> selectCategoryListWithContent() {

        List<Category> categoryList = categoryMapper.selectAll();
        // 剔除无内容栏目
        for (int i=0;i<categoryList.size();){
            Category c = categoryList.get(i);
            if (tagMapper.selectCountByCid(c.getCid())==0){
                categoryList.remove(c);
                continue;
            }
            i++;
        }
        return categoryList;
    }

    public Category selectByCid(Integer cid) {
        Category category = categoryMapper.selectByPrimaryKey(cid);
        if (category==null){
            throw new BlogException(ExceptionEnum.CATEGORY_NOT_FOUND);
        }
        return category;
    }
}
