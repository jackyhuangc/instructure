package com.jacky.common;

import com.jacky.common.config.EnvConfig;
import com.jacky.common.util.LogUtil;
import com.jacky.common.util.ThreadPoolUtil;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.RequestHandledEvent;

/**
 * Spring应用状态监听器
 *
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc 用于监听应用启动与关闭，如果一个bean实现了ApplicationListener接口，当一个ApplicationEvent 被发布以后，bean会自动被通知。
 **/
@Component
public class SpringApplicationListener implements ApplicationListener {
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ContextRefreshedEvent) {
            EnvConfig envConfig = SpringContextHolder.getBean(EnvConfig.class);

            // 监听到ContextRefreshedEvent事件，用于应用启动监控
            LogUtil.error(String.format("应用%s，版本%s 正在启动.....", envConfig.getSystemName(), envConfig.getVersion()));
        } else if (applicationEvent instanceof ContextStartedEvent) {
            // LogUtil.info("ContextStartedEvent......");
        } else if (applicationEvent instanceof ContextStoppedEvent) {
            // LogUtil.info("ContextStoppedEvent......");
        } else if (applicationEvent instanceof ContextClosedEvent) {

            EnvConfig envConfig = SpringContextHolder.getBean(EnvConfig.class);

            // LogUtil.info("监听到ContextClosedEvent事件，用于应用关闭监控......");
            LogUtil.error(String.format("应用%s，版本%s 正在关闭.....", envConfig.getSystemName(), envConfig.getVersion()));
        } else if (applicationEvent instanceof RequestHandledEvent) {
            // LogUtil.info("RequestHandledEvent......");
        }
    }
}
