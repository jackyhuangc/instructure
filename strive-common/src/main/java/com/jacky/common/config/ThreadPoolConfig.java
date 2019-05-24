package com.jacky.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置类
 *
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc
 **/
@Configuration
@Data
public class ThreadPoolConfig {

    public final static String TASK_POOL_NAME = "taskExecutor";

    @Value("${thread.corePoolSize:10}")
    private Integer corePoolSize;
    @Value("${thread.queueCapacity:20}")
    private Integer queueCapacity;
    @Value("${thread.maxPoolSize:20}")
    private Integer maxPoolSize;
    @Value("${thread.keepAliveSeconds:30}")
    private Integer keepAliveSeconds;

    @Bean(TASK_POOL_NAME)
    public ThreadPoolTaskExecutor getTaskExecutor() {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();

        //线程池维护线程的最少数量
        poolTaskExecutor.setCorePoolSize(corePoolSize);
        //线程池所使用的缓冲队列
        poolTaskExecutor.setQueueCapacity(queueCapacity);
        //线程池维护线程的最大数量
        poolTaskExecutor.setMaxPoolSize(maxPoolSize);
        //线程池维护线程所允许的空闲时间(默认60S)
        poolTaskExecutor.setKeepAliveSeconds(keepAliveSeconds);

        // 这个策略重试添加当前的任务，他会自动重复调用 execute() 方法，直到成功  在调用者的线程中(主线程)执行被拒绝的任务(主线程只有一个)
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        poolTaskExecutor.initialize();

        return poolTaskExecutor;
    }
}
