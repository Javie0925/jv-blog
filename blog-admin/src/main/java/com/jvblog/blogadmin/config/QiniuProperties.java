package com.jvblog.blogadmin.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author javie
 * @date 2019/6/11 19:11
 */
@Data
@ConfigurationProperties(prefix = "qiniu")
public class QiniuProperties {
    private String accessKey;

    private String secretKey;

    private String bucket;

    private String path;
}
