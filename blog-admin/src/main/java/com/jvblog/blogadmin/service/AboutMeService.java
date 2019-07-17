package com.jvblog.blogadmin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jvblog.service.enums.ExceptionEnum;
import com.jvblog.service.exception.BlogException;
import com.jvblog.service.mapper.AboutMeMapper;
import com.jvblog.service.pojo.AboutMe;
import com.jvblog.service.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author javie
 * @date 2019/6/16 1:01
 */
@Service
public class AboutMeService {

    @Autowired
    private AboutMeMapper aboutMeMapper;

    public AboutMe getAboutMe(){
        AboutMe aboutMe = aboutMeMapper.selectByPrimaryKey(1);
        return aboutMe;
    }

    @Transactional
    public void updateBasicInfo(AboutMe aboutMe) {

        int update = aboutMeMapper.updateByPrimaryKeySelective(aboutMe);
        if (update==0){
            throw new BlogException(ExceptionEnum.ABOUTME_UPDATE_FAIL);
        }
    }
    @Transactional
    public void deleteByName(String name) {
        // 获取aboutMe
        AboutMe aboutMe = aboutMeMapper.selectByPrimaryKey(1);
        // 获取contact json
        String contactJson = aboutMe.getContact();
        // 解析json
        List<Map<String, String>> contactList = JsonUtils.nativeRead(contactJson, new TypeReference<List<Map<String, String>>>() {});
        // 根据name删除
        for (int i=0;i<contactList.size();i++){
            Map<String,String> contact = contactList.get(i);
            if (contact.containsValue(name)){
                contactList.remove(contact);
                continue;
            }
        }
        // 转成json，存入数据库
        aboutMe.setContact(JsonUtils.serialize(contactList));
        int update = aboutMeMapper.updateByPrimaryKey(aboutMe);
        if (update==0){
            throw new BlogException(ExceptionEnum.ABOUTME_UPDATE_FAIL);
        }
    }
    @Transactional
    public void addContact(Map<String,String> contactMap) {
        // 获取aboutMe
        AboutMe aboutMe = aboutMeMapper.selectByPrimaryKey(1);
        // 获取contact json
        String contactJson = aboutMe.getContact();
        // 解析json
        List<Map<String, String>> contactList = JsonUtils.nativeRead(contactJson, new TypeReference<List<Map<String, String>>>() {});
        // 增加新联系方式
        contactList.add(contactMap);
        // 转成json，存入数据库
        aboutMe.setContact(JsonUtils.serialize(contactList));
        int update = aboutMeMapper.updateByPrimaryKey(aboutMe);
        if (update==0){
            throw new BlogException(ExceptionEnum.ABOUTME_ADD_FAIL);
        }
    }

    @Transactional
    public void updateText(AboutMe aboutMe) {
        int update = aboutMeMapper.updateByPrimaryKeySelective(aboutMe);
        if (update==0){
            throw new BlogException(ExceptionEnum.ABOUTME_UPDATE_FAIL);
        }
    }
}
