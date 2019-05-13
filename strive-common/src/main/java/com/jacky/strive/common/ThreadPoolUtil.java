package com.jacky.strive.common;


import com.jacky.strive.common.entity.Runnable1;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

public final class ThreadPoolUtil {
    private static ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();

    static {
        //线程池维护线程的最少数量
        poolTaskExecutor.setCorePoolSize(10);
        //线程池所使用的缓冲队列
        poolTaskExecutor.setQueueCapacity(100);
        //线程池维护线程的最大数量
        poolTaskExecutor.setMaxPoolSize(20);
        //线程池维护线程所允许的空闲时间(默认60S)
        poolTaskExecutor.setKeepAliveSeconds(3000);

        // 这个策略重试添加当前的任务，他会自动重复调用 execute() 方法，直到成功  在调用者的线程中(主线程)执行被拒绝的任务(主线程只有一个)
        poolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        poolTaskExecutor.initialize();
    }

    public static void execute(Runnable runnable) {
        poolTaskExecutor.execute(runnable);
    }

    public static <T1, T2> void execute(Runnable1<T1, T2> runnable, T1 t1, T2 t2) {
        poolTaskExecutor.execute(() -> runnable.run(t1, t2));
    }

    public static void shutdown() {
        poolTaskExecutor.shutdown();
    }
}

