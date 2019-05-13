package com.jacky.strive.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jacky.strive.common.StringUtil;
import lombok.NonNull;
import java.util.EnumSet;
import java.util.Set;

/**
 * Created by dy45 on 2017/7/22.
 * @param <T> data 数据类型
 */
public class ResResult<T> {
    
    private Integer code;
    private String message;
    private T data;
    
    @JsonIgnore
    private Set<ResultAction> actions;

    @JsonIgnore
    private Exception exception;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    
    /**
     * 不推荐使用该函数，请使用构造函数，该函数为反序列化准备
     * @param code
     */
    @Deprecated
    public void setCode(String code) {
        if (code == null) {
            return;
        }
        this.code = Integer.parseInt(code);
    }
    
    /**
     * 不推荐使用该函数，请使用构造函数，该函数为反序列化准备
     * @param code
     */
    public ResResult<T> setCode(Integer code) {
        this.code = code;
        return this;
    }


    public Exception getException() {
        return exception;
    }

    public ResResult<T> setException(Exception ex) {
        exception = ex;
        return this;
    }


    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    
    
    /**
     * 设置message
     * @param message
     * @return
     */
    public ResResult<T> setMessage(String message) {
        if (StringUtil.isEmtpy(this.message)
                && !StringUtil.isEmtpy(message)) {
            this.message = message;
        }
        return this;
    }

    /**
     * @return
     */
    @NonNull
    public Set<ResultAction> getActions() {
        if (actions == null) {
            actions = EnumSet.noneOf(ResultAction.class);
        }
        return actions;
    }

    public ResResult<T> setActions(Set<ResultAction> actions) {
        this.actions = actions;
        return this;
    }

    public ResResult(Integer code, String message, Set<ResultAction> actions) {
        this(code, message, null, actions);
    }

    public ResResult(Integer code, String message, T data, Set<ResultAction> actions) {
        this.code = code;
        this.message = message;
        this.actions = actions;
        this.data = data;
    }
    
    /**
     * @Deprecated 不推荐使用无参构造函数，请使用有参构造函数
     */
    @Deprecated
    public ResResult() {

    }

    public ResResult(String code, String message, T data, Set<ResultAction> actions) {
        this(Integer.parseInt(code), message, data, actions);
    }
    
    
    /**
     * 构造函数
     * @param code
     * @param message
     */
    public ResResult(Integer code, String message) {
        this(code, message, null);
    }


    @JsonIgnore
    public boolean isPostpone() {
        if (actions != null) {
            return actions.contains(ResultAction.NEED_POSTPONE);
        }
        return false;
    }

    @JsonIgnore
    public boolean isFail() {
        return ResultCode.FAILURE.equals(this.getCode());
    }

    @JsonIgnore
    public boolean isSuccess() {
        return ResultCode.SUCCESS.equals(this.getCode());
    }

    @JsonIgnore
    public boolean isProcess() {
        return !isSuccess() && !isFail();
    }

    @JsonIgnore
    public boolean canRetry() {
        return this.getActions().contains(ResultAction.NEED_POSTPONE)
                || this.getActions().contains(ResultAction.NEED_RETRY);
    }

    private static <T> ResResult<T> createResult(Integer code, String message, T data, Set<ResultAction> actions) {
        return new ResResult<>(code, message, data, actions);
    }

    public static ResResult success() {
        return success(null);
    }

    public static <T> ResResult<T> success(T data) {
        return createResult(ResultCode.SUCCESS, "", data, null);
    }

    public static ResResult success(String message) {
        return createResult(ResultCode.SUCCESS, message, null, null);
    }

    public static <T> ResResult<T> success(String message, T data) {
        return createResult(ResultCode.SUCCESS, message, data, null);
    }
    
    
    public static ResResult fail(String message) {
        return fail(message, null);
    }

    public static <T> ResResult<T> fail(String message, T data) {
        return createResult(ResultCode.FAILURE, message, data, null);
    }

    public static <T> ResResult<T> failNotice(String message, T data) {
        return createResult(ResultCode.FAILURE,
                message,
                data,
                EnumSet.of(ResultAction.NOTICE, ResultAction.WRITE_LOG));
    }

    public static ResResult failNotice(String message) {
        return failNotice(message, null);
    }

    public static <T> ResResult<T> process(String message, T data) {
        return createResult(ResultCode.PROCESS, message, data, null);
    }

    public static ResResult exception(String message, Exception ex) {
        ResResult resResult = retryNotice(message, null);
        resResult.exception = ex;
        return resResult;
    }

    public static <T> ResResult<T> process(String message, T data, Set<ResultAction> actions) {
        return createResult(ResultCode.PROCESS, message, data, actions);
    }

    public static ResResult suspend(String message) {
        return suspend(message, null);
    }

    public static <T> ResResult<T> suspend(String message, T data) {
        return createResult(ResultCode.PROCESS,
                message,
                data,
                EnumSet.of(ResultAction.NEED_POSTPONE));
    }

    public static <T> ResResult<T> retry(String message, T data) {
        return process(
                message,
                data,
                EnumSet.of(ResultAction.NEED_RETRY));
    }

    public static ResResult retry(String message) {
        return process(
                message,
                null,
                EnumSet.of(ResultAction.NEED_RETRY));
    }

    public static <T> ResResult<T> retryNotice(String message, T data) {
        return process(
                message,
                data,
                EnumSet.of(ResultAction.NEED_RETRY, ResultAction.NOTICE, ResultAction.WRITE_LOG));
    }

    public static ResResult retryNotice(String message) {
        return process(
                message,
                null,
                EnumSet.of(ResultAction.NEED_RETRY, ResultAction.NOTICE, ResultAction.WRITE_LOG));
    }
    

    public static <T> ResResult<T> waitOtherResult(String message, T data) {
        return process(message, data);
    }
    
    /**
     * 根据exception 返回
     * @param ex
     * @return
     */
    public static ResResult fromException(Throwable ex) {
        if (ex instanceof FailException) {
            return fail(ex.getMessage());
        } else if (ex instanceof FailNoticeException) {
            return failNotice(ex.getMessage(), null);
        } else if (ex instanceof RetryException) {
            return retry(ex.getMessage(), null);
        } else if (ex instanceof RetryNoticeException) {
            return retryNotice(ex.getMessage(), null);
        } else if (ex instanceof SuspendException) {
            return suspend(ex.getMessage());
        } else if (ex instanceof ProcessException) {
            return process(ex.getMessage(), null);
        }
        return null;
    }
}
