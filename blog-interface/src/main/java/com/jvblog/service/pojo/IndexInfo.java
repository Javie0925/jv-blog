package com.jvblog.service.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * @author javie
 * @date 2019/6/10 0:15
 */
@Data
@Table(name = "tb_index_info")
public class IndexInfo {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private Long toppingBlogId;

    @Transient
    private List<SlidePic> slidePicList;

    @Transient
    private BlogDetail blogDetail;

}
