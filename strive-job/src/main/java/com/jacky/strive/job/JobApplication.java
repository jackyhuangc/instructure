package com.jacky.strive.job;

import com.jacky.common.util.HttpUtil;
import com.jacky.common.util.LogUtil;
import com.jacky.common.util.ThreadPoolUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.jacky"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class JobApplication {

    public static void main(String[] args) {

        SpringApplication.run(JobApplication.class, args);

        //runLocalTask();
    }

    private static void runLocalTask() {
        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {

                        LogUtil.info("线程/quatos/sync执行中#####################");
                        HttpUtil.get("http://localhost:8082/job/quatos/sync", null);
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        LogUtil.error(e);
                    }
                }
            }
        });

        ThreadPoolUtil.execute(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {

                        LogUtil.info("线程/charge/sync执行中#####################");
                        HttpUtil.get("http://localhost:8082/job/charge/sync", null);
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        LogUtil.error(e);
                    }
                }
            }
        });

// 目前只用MCB
//        ThreadPoolUtil.execute(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//
//                        LogUtil.info("线程/charge/process执行中#####################");
//                        HttpUtil.get("http://localhost:8082/job/charge/process", null);
//                        Thread.sleep(1000);
//                    } catch (Exception e) {
//                        LogUtil.error(e);
//                    }
//                }
//            }
//        });

        ThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {

                        LogUtil.info("线程/interest/process执行中#####################");
                        HttpUtil.get("http://localhost:8082/job/interest/process", null);
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        LogUtil.error(e);
                    }
                }
            }
        });
    }
}