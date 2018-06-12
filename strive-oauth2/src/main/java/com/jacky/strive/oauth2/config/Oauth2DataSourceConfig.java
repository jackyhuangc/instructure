package com.jacky.strive.oauth2.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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

    @Value("${user}")
    private String username;

    @Value("${password}")
    private String password;

    @Value("${url}")
    private String url;
}
