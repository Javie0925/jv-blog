package com.jvblog.blogservice.service;

import com.jvblog.service.enums.ExceptionEnum;
import com.jvblog.service.exception.BlogException;
import com.jvblog.service.mapper.BlogpageInfoMapper;
import com.jvblog.service.mapper.VisitorMapper;
import com.jvblog.service.pojo.BlogpageInfo;
import com.jvblog.service.pojo.Visitor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author javie
 * @date 2019/6/10 12:19
 */
@Service
@Slf4j
public class VisitorService {

    @Autowired
    private VisitorMapper visitorMapper;
    @Autowired
    private BlogpageInfoMapper blogpageInfoMapper;

    /**
     * 保存访客信息
     * @param ip
     * @param address
     */
    @Transactional
    public void storeVisitorInfo(String ip, String address,String userAgent) {
            Visitor visitor = new Visitor();
            visitor.setIp(ip);
            visitor.setAddress(address);
            visitor.setVisitTime(new Date());
            visitor.setUserAgent(userAgent);
            int insert = visitorMapper.insert(visitor);
            if (insert==0){
                throw new BlogException(ExceptionEnum.VISITOR_UPDATE_FAIL);
            }
    }

    @Transactional
    public void addVisitor() {
        BlogpageInfo blogpageInfo = blogpageInfoMapper.selectByPrimaryKey(1);
        blogpageInfo.setVisit(blogpageInfo.getVisit()+1);
        // 保存新信息
        int update = blogpageInfoMapper.updateByPrimaryKey(blogpageInfo);
        //
        if (update==0){
            throw new BlogException(ExceptionEnum.VISITOR_UPDATE_FAIL);
        }

    }
}
