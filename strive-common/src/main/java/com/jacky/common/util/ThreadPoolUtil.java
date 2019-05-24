package com.jacky.common.util;


import com.jacky.common.SpringContextHolder;
import com.jacky.common.config.ThreadPoolConfig;
import com.jacky.common.entity.thread.Runnable1;
import com.jacky.common.entity.thread.Runnable2;

import java.util.*;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池工具类
 *
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc
 **/
public final class ThreadPoolUtil {

    private static ThreadPoolTaskExecutor threadPoolTaskExecutor;

    static {
        threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        //线程池维护线程的最少数量
        threadPoolTaskExecutor.setCorePoolSize(10);
        //线程池所使用的缓冲队列
        threadPoolTaskExecutor.setQueueCapacity(20);
        //线程池维护线程的最大数量
        threadPoolTaskExecutor.setMaxPoolSize(20);
        //线程池维护线程所允许的空闲时间(默认60S)
        threadPoolTaskExecutor.setKeepAliveSeconds(3000);

        // 这个策略重试添加当前的任务，他会自动重复调用 execute() 方法，直到成功  在调用者的线程中(主线程)执行被拒绝的任务(主线程只有一个)
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.initialize();
    }

    private static ThreadPoolTaskExecutor getPoolTaskExecutor() {

        ThreadPoolTaskExecutor poolTaskExecutor;

        if (null == SpringContextHolder.getApplicationContext()) {
            poolTaskExecutor = threadPoolTaskExecutor;
        } else {
            poolTaskExecutor = SpringContextHolder.getBean(ThreadPoolConfig.TASK_POOL_NAME, ThreadPoolTaskExecutor.class);
        }

        return poolTaskExecutor;
    }

    /**
     * 以线程池方式执行线程任务，单个任务
     *
     * @param runnable 任务执行方法
     */
    public static void execute(Runnable runnable) {
        getPoolTaskExecutor().execute(runnable);
    }

    /**
     * 以线程池方式执行线程任务，多个任务
     *
     * @param runnable 任务执行方法
     * @param list     任务列表
     * @param await    是否等待所有任务执行或执行完成
     * @param <T1>     任务参数
     */
    public static <T1> void execute(Runnable1<T1> runnable, List<T1> list, boolean await) {

        CountDownLatch countDownLatch = new CountDownLatch(list.size());

        list.forEach((t1) -> {
            getPoolTaskExecutor().execute(() -> {

                // 执行前计算，通常用于后台任务，大部分场景，缺点：任务未完成前不能主动shutdown，否则容易造成线程中断
                countDownLatch.countDown();

                runnable.run(t1);

                // countDownLatch.countDown(); 执行后计算，通常用于前台任务，需等待所有任务执行完整后，可以主动shutdown
                // 缺点：若某一任务未完成，将阻塞主流程
            });
        });

        if (await) {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void shutdown() {
        getPoolTaskExecutor().shutdown();
    }
}

