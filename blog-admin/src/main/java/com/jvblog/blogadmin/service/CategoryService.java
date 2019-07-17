package com.jvblog.blogadmin.service;

import com.jvblog.service.enums.ExceptionEnum;
import com.jvblog.service.exception.BlogException;
import com.jvblog.service.mapper.CategoryMapper;
import com.jvblog.service.pojo.Category;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author javie
 * @date 2019/7/13 1:05
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> selectCategoryList() {

        List<Category> categoryList = categoryMapper.selectAll();
        return categoryList;
    }

    public Category selectCategoryById(Integer cid) {
        Category category = categoryMapper.selectByPrimaryKey(cid);
        return category;
    }

    @Transactional
    public void deleteByCide(Integer cid) {
        int i = categoryMapper.deleteByPrimaryKey(cid);
        if (i==0){
            throw new BlogException(ExceptionEnum.CATEGORY_DELETE_FAIL);
        }
    }

    @Transactional
    public void update(Category category) {
        if(category==null || category.getCid()==null || StringUtils.isEmpty(category.getCname())){
            throw new BlogException(ExceptionEnum.BAD_REQUEST_PARAMS);
        }
        int update = categoryMapper.updateByPrimaryKey(category);
        if (update==0){
            throw new BlogException(ExceptionEnum.CATEGORY_UPDATE_FAIL);
        }
    }

    @Transactional
    public void addCategory(String cname) {
        if (cname==null || StringUtils.isEmpty(cname)){
            throw new BlogException(ExceptionEnum.BAD_REQUEST_PARAMS);
        }
        Category category = new Category();
        category.setCname(cname);
        int insert = categoryMapper.insertSelective(category);
        if (insert==0){
            throw new BlogException(ExceptionEnum.CATEGORY_ADD_FAIL);
        }
    }
}
