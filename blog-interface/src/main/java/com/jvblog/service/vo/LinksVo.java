package com.jvblog.service.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author javie
 * @date 2019/6/13 14:05
 */
@Data
public class LinksVo {

    private List<Map<String,String>> data;

    private boolean show = true;
}
