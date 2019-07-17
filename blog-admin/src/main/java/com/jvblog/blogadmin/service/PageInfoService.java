package com.jvblog.blogadmin.service;

import com.jvblog.service.enums.ExceptionEnum;
import com.jvblog.service.exception.BlogException;
import com.jvblog.service.mapper.BlogDetailMapper;
import com.jvblog.service.mapper.BlogpageInfoMapper;
import com.jvblog.service.mapper.IndexInfoMapper;
import com.jvblog.service.mapper.SlidePicMapper;
import com.jvblog.service.pojo.BlogDetail;
import com.jvblog.service.pojo.BlogpageInfo;
import com.jvblog.service.utils.JsonUtils;
import com.jvblog.service.vo.LinksVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author javie
 * @date 2019/6/13 13:22
 */
@Service
public class PageInfoService {

    @Autowired
    private BlogpageInfoMapper blogpageInfoMapper;
    @Autowired
    private IndexInfoMapper indexInfoMapper;
    @Autowired
    private BlogDetailMapper blogDetailMapper;
    @Autowired
    private SlidePicMapper slidePicMapper;

    public BlogpageInfo getBlogpageInfo(){
        // 获取blogpageInfo
        BlogpageInfo blogpageInfo = blogpageInfoMapper.selectByPrimaryKey(1);
        // 获取blogDetail
        BlogDetail blogDetail = blogDetailMapper.selectByPrimaryKey(blogpageInfo.getToppingBlogId());
        blogpageInfo.setBlogDetail(blogDetail);
        return blogpageInfo;
    }

    @Transactional
    public void addLink(String name, String ip) {
        // 判空
        if (StringUtils.isEmpty(name)||StringUtils.isEmpty(ip)){
            throw new BlogException(ExceptionEnum.BAD_REQUEST_PARAMS);
        }
        // 获取blogpageinfo
        BlogpageInfo blogpageInfo = blogpageInfoMapper.selectByPrimaryKey(1);
        // 解析link json
        LinksVo linksVo = JsonUtils.toBean(blogpageInfo.getLinks(), LinksVo.class);
        // 判重
        linksVo.getData().forEach(linkMap ->{

            if (linkMap.containsKey(name)){
                throw new BlogException(ExceptionEnum.LINK_ADD_FAIL);
            }
        } );
        // 添加新link
        List<Map<String, String>> data = linksVo.getData();
        Map<String,String> newLink = new HashMap<>();
        newLink.put("name", name);
        newLink.put("ip", ip);
        data.add(newLink);
        linksVo.setData(data);
        blogpageInfo.setLinks(JsonUtils.serialize(linksVo));
        // 保存
        int insert = blogpageInfoMapper.updateByPrimaryKey(blogpageInfo);
        if (insert==0){
            throw new BlogException(ExceptionEnum.LINK_ADD_FAIL);
        }

    }
    @Transactional
    public void deleteLinkByName(String name) {
        // 判空
        if (StringUtils.isEmpty(name)){
            throw new BlogException(ExceptionEnum.BAD_REQUEST_PARAMS);
        }
        // 获取blogpageinfo
        BlogpageInfo blogpageInfo = blogpageInfoMapper.selectByPrimaryKey(1);
        // 解析link json
        LinksVo linksVo = JsonUtils.toBean(blogpageInfo.getLinks(), LinksVo.class);
        // 删除link
        List<Map<String, String>> list = linksVo.getData();
        for (int i=0;i<list.size();i++){
            if (list.get(i).containsValue(name)){
                list.remove(list.get(i));
                continue;
            }
        }
        // 保存
        linksVo.setData(list);
        if(JsonUtils.serialize(linksVo).contains(name)){

            throw new BlogException(ExceptionEnum.LINK_DELETE_FAIL);
        }
        blogpageInfo.setLinks(JsonUtils.serialize(linksVo));
        int update = blogpageInfoMapper.updateByPrimaryKey(blogpageInfo);
        if (update==0){
        }
    }

    @Transactional
    public void updateHeaderMsg(String name, String text) {
        BlogpageInfo blogpageInfo = blogpageInfoMapper.selectByPrimaryKey(1);
        // 解析headerMsg json
        Map<String, String> headerMsgMap = JsonUtils.toMap(blogpageInfo.getHeaderMsg(), String.class, String.class);
        // 修改
        headerMsgMap.remove(name);
        headerMsgMap.put(name, text);
        // 保存
        blogpageInfo.setHeaderMsg(JsonUtils.serialize(headerMsgMap));
        int update = blogpageInfoMapper.updateByPrimaryKey(blogpageInfo);
        if (update==0){
            throw new BlogException(ExceptionEnum.HEADER_MSG_UPDATE_FAIL);
        }
    }
    @Transactional
    public void updateBlogpageInfo(BlogpageInfo blogpageInfo) {

        int update = blogpageInfoMapper.updateByPrimaryKeySelective(blogpageInfo);
        if (update==0){
            throw new BlogException(ExceptionEnum.BLOGPAGE_INFO_UPDATE_FAIL);
        }
    }
}
