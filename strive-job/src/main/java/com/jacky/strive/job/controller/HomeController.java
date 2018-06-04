package com.jacky.strive.job.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description Here...
 *
 * @author Jacky Huang
 * @date 2018/5/17 18:44
 * @since jdk1.8
 */
@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("")
    public String index() {
        return "Hello, xxl-job-strive executor!";
    }
}
