package com.jacky.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Spring上下文工具类
 *
 * @author huangchao
 * @create 2018/6/6 下午1:55
 * @desc 获取系统初始化自动注入的bean对象
 **/
@Component
public class SpringContextHolder implements ApplicationContextAware {

    /**
     * Spring的 【ApplicationContext】 提供了支持事件和代码中监听器的功能。
     * Spring容器会检测容器中的所有Bean，如果发现某个Bean实现了ApplicationContextAware接口，Spring容器会在创建该Bean之后，
     * 自动调用该Bean的setApplicationContextAware()方法，调用该方法时，会将容器本身作为参数传给该方法——该方法中的实现部分
     * 将Spring传入的参数（容器本身）赋给该类对象的applicationContext实例变量，因此接下来可以通过该applicationContext实例变量来访问容器本身。
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(String name, Class<T> var2) throws BeansException {
        return applicationContext.getBean(name, var2);
    }

    public static <T> T getBean(Class<T> aClass) throws BeansException {
        return applicationContext.getBean(aClass);
    }

    public static <T> T getBean(Class<T> aClass, Object... objects) {
        return applicationContext.getBean(aClass, objects);
    }

    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }
}
