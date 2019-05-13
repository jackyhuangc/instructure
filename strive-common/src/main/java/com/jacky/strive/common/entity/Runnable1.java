package com.jacky.strive.common.entity;

@FunctionalInterface
public interface Runnable1<T1,T2> {
    void run(T1 t1,T2 t2);
}