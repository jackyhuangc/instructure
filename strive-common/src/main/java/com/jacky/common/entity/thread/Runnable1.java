package com.jacky.common.entity.thread;

@FunctionalInterface
public interface Runnable1<T1> {
    void run(T1 t1);
}