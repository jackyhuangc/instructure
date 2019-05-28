package com.jacky.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 环境参数配置类
 *
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc
 **/
@Configuration
@Data
public class EnvConfig {

    @Value("${env.appName}")
    private String appName;

    @Value("${env.version}")
    private String version;
}
