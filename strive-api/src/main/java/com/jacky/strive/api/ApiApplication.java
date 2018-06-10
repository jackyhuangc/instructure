package com.jacky.strive.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.error.DefaultOAuth2ExceptionRenderer;

@SpringBootApplication
@EnableResourceServer
@ComponentScan(basePackages = {"com.jacky"})
//@EnableAutoConfiguration( exclude = {DataSourceAutoConfiguration.class})
public class ApiApplication {

    public static void main(String[] args) {
        //DefaultOAuth2ExceptionRenderer
        // 该属性解决elasticsearch的availableProcessors is already set to [4], rejecting [4]问题
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(ApiApplication.class, args);
    }
}
