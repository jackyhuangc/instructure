package com.jacky.strive.common.entity;


/**
 * 指示 task runner收到对应action后，对应的操作.
 */
public enum ResultAction {
    NOTICE,
    WRITE_LOG,
    NEED_RETRY,
    NEED_POSTPONE
}
