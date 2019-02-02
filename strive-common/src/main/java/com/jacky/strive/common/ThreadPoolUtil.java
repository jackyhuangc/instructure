package com.jacky.strive.common;


import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @Author:denny
 * @Description:
 * @Date:2018/4/20
 */
public final class ThreadPoolUtil {

    private ThreadPoolUtil() {
    }

    private static ThreadPoolTaskExecutor getExecutor() {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setQueueCapacity(10000);
        poolTaskExecutor.setCorePoolSize(5);
        poolTaskExecutor.setMaxPoolSize(10);
        poolTaskExecutor.setKeepAliveSeconds(5000);
        poolTaskExecutor.initialize();

        return poolTaskExecutor;
    }

    /**
     * 使用线程池线程，异步执行
     *
     * @param runnable 待执行任务
     */
    public static void execute(Runnable runnable) {
        getExecutor().execute(() -> {
            try {
                runnable.run();
            } catch (Exception ex) {
                LogUtil.error(ex);
            }
        });
    }

    /**
     * 使用线程池线程，异步执行
     *
     * @param runnable 待执行任务
     * @param t1       任务参数
     * @param <T>      任务参数
     */
    public static <T> void execute(Runnable runnable, T t1) {
        execute(() -> runnable.run());
    }
}

