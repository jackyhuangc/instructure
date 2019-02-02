package com.jacky.strive.api;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jacky.strive.dao.UserDao;
import com.jacky.strive.dao.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jacky"})
@EnableAutoConfiguration( exclude = {DataSourceAutoConfiguration.class})
public class ApiApplication {

    public static void main(String[] args) {
        //new test().testt();
        //DefaultOAuth2ExceptionRenderer
        // 该属性解决elasticsearch的availableProcessors is already set to [4], rejecting [4]问题
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(ApiApplication.class, args);
    }
}