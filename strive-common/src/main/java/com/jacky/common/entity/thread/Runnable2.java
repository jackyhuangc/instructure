package com.jacky.common.entity.thread;

@FunctionalInterface
public interface Runnable2<T1,T2> {
    void run(T1 t1,T2 t2);
}