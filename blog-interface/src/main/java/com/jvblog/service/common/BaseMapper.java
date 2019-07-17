package com.jvblog.service.common;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author javie
 * @date 2019/6/2 22:28
 */
public interface BaseMapper<T,K> extends Mapper<T>,IdListMapper<T,K>,IdsMapper<T> {
}
