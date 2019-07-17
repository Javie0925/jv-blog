package com.jvblog.blogadmin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jvblog.service.enums.ExceptionEnum;
import com.jvblog.service.exception.BlogException;
import com.jvblog.service.mapper.VisitorMapper;
import com.jvblog.service.pojo.BlogDetail;
import com.jvblog.service.pojo.Visitor;
import com.jvblog.service.utils.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author javie
 * @date 2019/6/14 21:19
 */
@Service
public class VisitorService {

    @Autowired
    private VisitorMapper visitorMapper;

    /**
     * 按照关键字和分页参数查询访客列表
     * @param pageNum
     * @param pageSize
     * @param sortBy
     * @param desc
     * @return
     */
    public PageResult<Visitor> queryVisitorsByPage(
            Integer pageNum,Integer pageSize,String sortBy,Boolean desc){

        // 分页
        PageHelper.startPage(pageNum, pageSize);

        // 排序
        Example example = new Example(BlogDetail.class);
        if(StringUtils.isNotBlank(sortBy)){
            String orderByClause = sortBy + (desc?" desc":" asc");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        List<Visitor> visitorList = visitorMapper.selectByExample(example);
        // 处理结果
        PageInfo<Visitor> pageInfo = new PageInfo<>(visitorList);

        Long totalPage = pageInfo.getTotal()%pageSize==0?pageInfo.getTotal()/pageSize:pageInfo.getTotal()/pageSize+1;
        // 返回结果
        return new PageResult<Visitor>(pageInfo.getTotal(),totalPage,visitorList,pageNum,pageSize);
    }
    @Transactional
    public void deleteVisitorById(Long id) {
        int delete = visitorMapper.deleteByPrimaryKey(id);
        if (delete==0){
            throw new BlogException(ExceptionEnum.VISITOR_DELETE_FAIL);
        }
    }

    public void deleteVisitorByIds(List<Long> ids) {
        int deleteByIdList = visitorMapper.deleteByIdList(ids);
        if (deleteByIdList==0){
            throw new BlogException(ExceptionEnum.VISITOR_DELETE_FAIL);
        }
    }
}
