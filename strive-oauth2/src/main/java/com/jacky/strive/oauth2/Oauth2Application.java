package com.jacky.strive.oauth2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @EnableResourceServer 只做认证服务器，不做资源服务器，以免混淆
 */
@EnableResourceServer
@SpringBootApplication
@ComponentScan(basePackages = {"com.jacky","com.jacky.strive.oauth2.config"})
public class Oauth2Application {

    public static void main(String[] args) {

        // 该属性解决elasticsearch的availableProcessors is already set to [4], rejecting [4]问题
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(Oauth2Application.class, args);
    }
}
