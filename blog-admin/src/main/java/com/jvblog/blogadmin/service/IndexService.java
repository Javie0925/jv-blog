package com.jvblog.blogadmin.service;

import com.jvblog.blogadmin.interceptor.LoginInterceptor;
import com.jvblog.blogadmin.mapper.AdministratorMapper;
import com.jvblog.blogadmin.pojo.Administrator;
import com.jvblog.blogadmin.pojo.IndexVo;
import com.jvblog.blogadmin.pojo.UserInfo;
import com.jvblog.service.mapper.BlogDetailMapper;
import com.jvblog.service.mapper.BlogpageInfoMapper;
import com.jvblog.service.mapper.CommentMapper;
import com.jvblog.service.mapper.VisitorMapper;
import com.jvblog.service.pojo.BlogpageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author javie
 * @date 2019/6/17 21:49
 */
@Service
public class IndexService {

    @Autowired
    private BlogDetailMapper blogDetailMapper;
    @Autowired
    private BlogpageInfoMapper blogpageInfoMapper;
    @Autowired
    private VisitorMapper visitorMapper;
    @Autowired
    private AdministratorMapper administratorMapper;
    @Autowired
    private CommentMapper commentMapper;


    public IndexVo queryIndexData() {

        // 获取登陆用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //private int blogCount;
        int blogCount = blogDetailMapper.selectBlogCount();
        //private int commentCount;

        //private int flinkCount;
        BlogpageInfo blogpageInfo = blogpageInfoMapper.selectByPrimaryKey(1);
        int flinkCount = 0;
        String linksStr = blogpageInfo.getLinks();
        for (int i=0;i<linksStr.length();) {
            if(linksStr.indexOf("name", i)!=-1){
                flinkCount++;
                i = linksStr.indexOf("name", i)+4;
            }else{
                break;
            }
        }
        //private int visitCount;
        int visitorCount = visitorMapper.selectVisitorCount();
        //private int loginCount;
        Administrator administrator = administratorMapper.selectByPrimaryKey(userInfo.getId());
        Integer loginCount = administrator.getLoginCount();
        //private int administratorCount;
        int administratorCount = administratorMapper.selectAvilableAdministratorCount();
        //private Date lastLoginTime;
        int commentCount = commentMapper.selectAllCount();

        // 封装数据
        IndexVo indexVo = new IndexVo();
        indexVo.setBlogCount(blogCount);
        indexVo.setAdministratorCount(administratorCount);
        indexVo.setFlinkCount(flinkCount);
        indexVo.setVisitorCount(visitorCount);
        indexVo.setLoginCount(loginCount);
        indexVo.setLastLoginTime(administrator.getLastLoginTime());
        indexVo.setLastLoginIp(administrator.getLastLoginIp());
        indexVo.setCurrentLoginIp(administrator.getCurrentLoginIp());
        indexVo.setCommentCount(commentCount);


        return indexVo;
    }
}
