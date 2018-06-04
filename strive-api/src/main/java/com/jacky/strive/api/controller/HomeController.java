package com.jacky.strive.api.controller;

import com.jacky.strive.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/5/5 21:27
 * @since jdk1.8
 */
@RestController
public class HomeController {
    @Autowired
    UserService userService;

    @GetMapping("/test")
    public String testConnect() {
        Logger logger = LoggerFactory.getLogger(HomeController.class);
        logger.warn("sentry错误测试。。。");
        return userService.testConnnect();
    }
}
