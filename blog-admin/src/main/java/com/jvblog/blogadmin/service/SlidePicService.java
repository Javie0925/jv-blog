package com.jvblog.blogadmin.service;

import com.jvblog.service.enums.ExceptionEnum;
import com.jvblog.service.exception.BlogException;
import com.jvblog.service.mapper.SlidePicMapper;
import com.jvblog.service.pojo.Blog;
import com.jvblog.service.pojo.SlidePic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author javie
 * @date 2019/6/14 11:32
 */
@Service
public class SlidePicService {

    @Autowired
    private SlidePicMapper slidePicMapper;

    public List<SlidePic> getSlidePicList(){

        return slidePicMapper.selectAll();
    }

    @Transactional
    public void alterSlidePicState(Integer id) {
        // 获取
        SlidePic slidePic = slidePicMapper.selectByPrimaryKey(id);
        // 修改状态
        slidePic.setVisible(!slidePic.getVisible());
        // 保存
        int update = slidePicMapper.updateByPrimaryKey(slidePic);
        // 判断
        if (update==0){
            throw new BlogException(ExceptionEnum.SLIDE_PIC_UPDATE_FAIL);
        }
    }

    public void updateSlidePic(SlidePic slidePic) {
        //判空
        if (StringUtils.isEmpty(slidePic.getUrl())){
            throw new BlogException(ExceptionEnum.SLIDE_PIC_UPDATE_FAIL);
        }
        // 保存
        int update = slidePicMapper.updateByPrimaryKeySelective(slidePic);
        if (update==0){
            throw new BlogException(ExceptionEnum.SLIDE_PIC_UPDATE_FAIL);
        }
    }

    @Transactional
    public void addSlidePic(SlidePic slidePic) {
        // 判空
        if(StringUtils.isEmpty(slidePic.getUrl())){
            throw new BlogException(ExceptionEnum.SLIDE_PIC_ADD_FAIL);
        }
        // 保存信息
        int insert = slidePicMapper.insert(slidePic);
        if (insert==0){
            throw new BlogException(ExceptionEnum.SLIDE_PIC_ADD_FAIL);
        }
    }

    public void deleteById(Integer id) {
        int delete = slidePicMapper.deleteByPrimaryKey(id);
        if (delete==0){
            throw new BlogException(ExceptionEnum.SLIDE_PIC_DEL_FAIL);
        }
    }
}
