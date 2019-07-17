package com.jvblog.blogadmin.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jvblog.service.enums.ExceptionEnum;
import com.jvblog.service.exception.BlogException;
import com.jvblog.service.mapper.MessageMapper;
import com.jvblog.service.pojo.BlogDetail;
import com.jvblog.service.pojo.Message;
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
 * @date 2019/6/9 21:06
 */
@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    /**
     * 分页查询留言
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageResult<Message> queryMessageByPage(Integer pageNum,Integer pageSize) {
        // 分页
        PageHelper.startPage(pageNum, pageSize);
        // 排序
        Example example = new Example(Message.class);
        String orderByClause = "create_time desc";
        example.setOrderByClause(orderByClause);
        // 查询
        List<Message> messageList = messageMapper.selectByExample(example);

        PageInfo<Message> pageInfo = new PageInfo<>(messageList);

        Long totalPage = pageInfo.getTotal()%pageSize==0?pageInfo.getTotal()/pageSize:pageInfo.getTotal()/pageSize+1;
        return new PageResult<Message>(pageInfo.getTotal(),totalPage,messageList,pageNum,pageSize);
    }

    public PageResult<Message> queryMsgsByKeyAndPage(Integer pageNum, Integer pageSize, String sortBy, String key, Boolean desc) {

        // 分页
        PageHelper.startPage(pageNum, pageSize);

        // 排序
        Example example = new Example(BlogDetail.class);
        if(StringUtils.isNotBlank(sortBy)){
            String orderByClause = sortBy + (desc?" desc":" asc");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        List<Message> messageList = messageMapper.selectByExample(example);
        // 判空
        if(CollectionUtils.isEmpty(messageList)){
            throw new BlogException(ExceptionEnum.MESSAGE_NOT_FOUND);
        }
        PageInfo<Message> pageInfo = new PageInfo<>(messageList);

        Long totalPage = pageInfo.getTotal()%pageSize==0?pageInfo.getTotal()/pageSize:pageInfo.getTotal()/pageSize+1;

        return new PageResult<Message>(pageInfo.getTotal(),totalPage,messageList,pageNum,pageSize);
    }

    @Transactional
    public void deleteMsgByIds(List<Long> msgIds) {

        if(msgIds==null||msgIds.size()==0){
            throw new BlogException(ExceptionEnum.MESSAGE_DELETE_FAIL);
        }
        int i = messageMapper.deleteByIdList(msgIds);
        if (i==0){
            throw new BlogException(ExceptionEnum.MESSAGE_DELETE_FAIL);
        }
    }
}
