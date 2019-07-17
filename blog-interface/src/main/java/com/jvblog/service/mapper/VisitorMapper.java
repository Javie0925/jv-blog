package com.jvblog.service.mapper;

import com.jvblog.service.common.BaseMapper;
import com.jvblog.service.pojo.Visitor;
import org.apache.ibatis.annotations.Select;

/**
 * @author javie
 * @date 2019/6/10 12:19
 */
public interface VisitorMapper extends BaseMapper<Visitor,Long> {

    @Select("SELECT COUNT(*) FROM tb_visitor")
    int selectVisitorCount();
}
