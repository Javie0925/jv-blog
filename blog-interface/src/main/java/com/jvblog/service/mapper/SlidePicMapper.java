package com.jvblog.service.mapper;

import com.jvblog.service.common.BaseMapper;
import com.jvblog.service.pojo.SlidePic;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author javie
 * @date 2019/6/14 0:07
 */
public interface SlidePicMapper extends BaseMapper<SlidePic,Integer> {

    @Select("SELECT * FROM tb_slide_pics WHERE visible=true")
    List<SlidePic> selectVisible();

}
