package com.jvblog.blogservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jvblog.service.mapper.BlogDetailMapper;
import com.jvblog.service.pojo.*;
import com.jvblog.service.utils.JsonUtils;
import com.jvblog.service.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author javie
 * @date 2019/6/18 21:52
 */
@Service
public class RedisSevice {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private BlogDetailMapper blogDetailMapper;

    private String indexDataKey = "jvblog:indexdata";

    public Map<String,Object> getIndexDataFromRedis() throws Exception{
        // 创建一个hash
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(indexDataKey);
        // 判断是否有值
        if (!hashOps.hasKey(indexDataKey)) {
            // 无值，返回空
            return null;
        }
        // 有值，取出
        PageResult<BlogDetail> pageResult
                = JsonUtils.nativeRead(hashOps.get("pageResult").toString(), new TypeReference<PageResult<BlogDetail>>(){});
        // 更新blogDetail的watch值
        List<BlogDetail> itemList = pageResult.getItemList();
        itemList.forEach(blogDetail -> {
            blogDetail.setWatch(blogDetailMapper.selectWatchById(blogDetail.getBlogId()));
        });
        pageResult.setItemList(itemList);

        List<Tag> tagList = JsonUtils.nativeRead(hashOps.get("tagList").toString(), new TypeReference<List<Tag>>(){});
        AboutMe aboutMe = JsonUtils.toBean(hashOps.get("aboutMe").toString(), AboutMe.class);
        BlogpageInfo blogpageInfo = JsonUtils.toBean(hashOps.get("blogpageInfo").toString(), BlogpageInfo.class);
        List<SlidePic> slidePicList = JsonUtils.nativeRead(hashOps.get("slidePicList").toString(), new TypeReference<List<SlidePic>>(){});
        // 保存数据到map
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("pageResult", pageResult);
        dataMap.put("tagList", tagList);
        dataMap.put("aboutMe", aboutMe);
        dataMap.put("blogpageInfo", blogpageInfo);
        dataMap.put("slidePicList", slidePicList);
        return dataMap;
    }

    // 保存主页数据到redis
    public void insertIndexDataToRedis(Map<String,Object> dataMap) throws Exception{

        // 创建一个hash
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(indexDataKey);
        // 获取keySet
        Set<String> keySet = dataMap.keySet();
        // 将数据存入redis
        keySet.forEach(key->{
            hashOps.put(key, JsonUtils.serialize(dataMap.get(key)));
        });
    }
}
