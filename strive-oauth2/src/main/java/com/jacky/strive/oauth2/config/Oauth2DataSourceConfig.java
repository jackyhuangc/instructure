package com.jacky.strive.oauth2.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author huangchao
 * @create 2018/6/11 下午5:23
 * @desc
 **/

@Component
@PropertySource( value = {"classpath:oauth2.yml"})
@ConfigurationProperties("oauth2.datasource")
@Data
public class Oauth2DataSourceConfig {

    // ${username} ${user} 分别会取到windows,mac用户名？？？
    @Value("${username}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${url}")
    private String url;
}
