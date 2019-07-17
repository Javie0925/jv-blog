package com.jvblog.service.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author javie
 * @date 2019/6/3 14:03
 */
@Data
@AllArgsConstructor
public class PageResult<T> {

    private Long total;
    private Long totalPage;
    private List<T> itemList;
    private Integer currentPage;
    private Integer pageSize;

}
