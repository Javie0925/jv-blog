package com.jvblog.service.mapper;


import com.jvblog.service.common.BaseMapper;
import com.jvblog.service.pojo.Tag;
import org.apache.ibatis.annotations.Select;

/**
 * @author javie
 * @date 2019/6/4 13:21
 */
public interface TagMapper extends BaseMapper<Tag,Integer> {

    @Select("SELECT COUNT(*) FROM tb_tag WHERE cid = #{cid}")
    int selectCountByCid(Integer cid);

    @Select("SELECT tag_id FROM tb_tag WHERE cid=#{cid}")
    int[] selectTagIdsByCid(Integer cid);
}
