package com.jvblog.blogadmin.service;

import com.jvblog.blogadmin.config.JwtProperties;
import com.jvblog.blogadmin.mapper.AdministratorMapper;
import com.jvblog.blogadmin.pojo.Administrator;
import com.jvblog.blogadmin.pojo.UserInfo;
import com.jvblog.blogadmin.utils.CodecUtils;
import com.jvblog.blogadmin.utils.JwtUtils;
import com.jvblog.service.enums.ExceptionEnum;
import com.jvblog.service.exception.BlogException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.Map;

/**
 * @author javie
 * @date 2019/6/16 15:24
 */
@Slf4j
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private AdministratorMapper administratorMapper;
    @Autowired
    private JwtProperties jwtProperties;

    @Transactional
    public void addAdministrator(Map<String, String> paramsMap) {
        // 校验用户名是否存在
        Administrator exsit = administratorMapper.selectByUsername(paramsMap.get("username"));
        if (exsit!=null){
            throw new BlogException(ExceptionEnum.ADMINISTRATOR_ADD_FAIL);
        }
        // 创建空白Administrator
        Administrator administrator = new Administrator();
        //生成盐
        String salt = CodecUtils.generateSalt();
        // 加密密码
        String md5Password = CodecUtils.md5Hex(paramsMap.get("password"), salt);
        // 补全数据
        administrator.setUsername(paramsMap.get("username"));
        administrator.setPassword(md5Password);
        administrator.setSalt(salt);
        administrator.setUserReason(paramsMap.get("userReason"));
        administrator.setCreateTime(new Date());
        // 保存
        int insertSelective = administratorMapper.insertSelective(administrator);
        if (insertSelective==0){
            throw new BlogException(ExceptionEnum.ADMINISTRATOR_ADD_FAIL);
        }
    }

    public String login(Map<String, String> userMap,String ip) {

        // 根据用户名获取用户信息
        // (无效!)Administrator administrator = administratorMapper.selectByUsername(userMap.get("username"));
        Example example = new Example(Administrator.class);
        example.createCriteria().andEqualTo("username", userMap.get("username"));
        Administrator administrator = administratorMapper.selectByExample(example).get(0);
        System.out.println("####################################"+administrator);
        try {
            // 校验用户名
            if (administrator == null) {
                log.error("[授权中心] 登陆失败，用户名不存在");
                throw new BlogException(ExceptionEnum.LOGIN_FAIL);
            }
            // 校验审核状态
            if (!administrator.getAvilable()) {
                log.error("[授权中心] 登陆失败，用户未审核");
                throw new BlogException(ExceptionEnum.LOGIN_FAIL);
            }
            // 校验密码
            String md5Password = CodecUtils.md5Hex(userMap.get("password"), administrator.getSalt());
            if (!md5Password.equals(administrator.getPassword())) {
                log.error("[授权中心] 登陆失败，密码错误！用户名:{}", administrator.getUsername());
                throw new BlogException(ExceptionEnum.LOGIN_FAIL);
            }
            try {

                // 校验成功，记录登陆状态
                administrator.setLoginCount(administrator.getLoginCount()+1);
                administrator.setLastLoginTime(administrator.getCurrentLoginTime());
                administrator.setCurrentLoginTime(new Date());
                administrator.setLastLoginIp(administrator.getCurrentLoginIp());
                administrator.setCurrentLoginIp(ip);
                // 保存登陆状态
                administratorMapper.updateByPrimaryKeySelective(administrator);
            }catch (Exception e){
                log.error("[授权中心] 保存登陆状态失败，用户名：{}",administrator.getUsername(),e);
            }
            // 生成token
            UserInfo userInfo = new UserInfo(administrator.getId(), administrator.getUsername());
            String token = JwtUtils.generateToken(userInfo, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
            // 返回token
            return token;
        }catch (Exception e){
            log.error("[授权中心] 生成token失败，用户名：{}",administrator.getUsername(),e);
            throw new BlogException(ExceptionEnum.TOKEN_CREATE_ERROR);
        }

    }

    @Transactional
    public void updateUserInfo(Map<String, String> paramsMap) {
        // 根据id获取用户信息
        Administrator administrator = administratorMapper.selectByPrimaryKey(paramsMap.get("id"));
        // 验证新用户名是否可用
        Administrator existing = administratorMapper.selectByUsername(paramsMap.get("username"));
        if (existing!=null){
            // 用户名存在，是不是同一个用户
            if (!existing.getPassword().equals(CodecUtils.md5Hex(paramsMap.get("oldPassword"), administrator.getSalt()))){
                // 不是同一个用户，用户名不可用，抛异常
                throw new BlogException(ExceptionEnum.INVALID_USERNAME);
            }
        }
        // 验证旧密码
        String md5Password = CodecUtils.md5Hex(paramsMap.get("oldPassword"), administrator.getSalt());
        if (!md5Password.equals(administrator.getPassword())){
            throw new BlogException(ExceptionEnum.WRONG_PASSWORD);
        }
        //生成盐
        String salt = CodecUtils.generateSalt();
        // 加密密码
        String newMd5Password = CodecUtils.md5Hex(paramsMap.get("newPassword"), salt);
        // 修改信息
        administrator.setUsername(paramsMap.get("username"));
        administrator.setSalt(salt);
        administrator.setPassword(newMd5Password);
        administrator.setUpdateTime(new Date());
        // 保存信息
        int update = administratorMapper.updateByPrimaryKeySelective(administrator);
        if (update==0){
            throw new BlogException(ExceptionEnum.USERINFO_UPDATE_FAIL);
        }
    }
}
