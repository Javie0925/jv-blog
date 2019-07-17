package com.jvblog.blogadmin.mapper;

import com.jvblog.blogadmin.pojo.Administrator;
import com.jvblog.service.common.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author javie
 * @date 2019/6/16 15:31
 */
public interface AdministratorMapper extends BaseMapper<Administrator,Integer> {

    @Select("SELECT * FROM tb_administrator WHERE username=#{username}")
    Administrator selectByUsername(String username);
    @Select("SELECT COUNT(*) FROM tb_administrator WHERE avilable = true")
    int selectAvilableAdministratorCount();
}
