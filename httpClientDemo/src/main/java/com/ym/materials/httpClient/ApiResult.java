package com.ym.materials.httpClient;

import java.io.Serializable;

/**
 * Created by ym on 2018/5/17.
 */
public class ApiResult<T> implements Serializable {
    private boolean success;
    private int code;
    private String errMsg;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public ApiResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public int getCode() {
        return code;
    }

    public ApiResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public ApiResult setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }

    public T getData() {
        return data;
    }

    public ApiResult setData(T data) {
        this.data = data;
        return this;
    }

    public static <T> ApiResult<T> buildSussess(T data) {
        ApiResult<T> result = new ApiResult<>();
        result.setSuccess(true);
        result.setData(data);
        return result;
    }

    public static <T> ApiResult<T> buildSussess() {
        ApiResult<T> result = new ApiResult<>();
        result.setSuccess(true);
        return result;
    }

    public static <T> ApiResult<T> buildFailure() {
        ApiResult<T> result = new ApiResult<>();
        result.setSuccess(false);
        return result;
    }

    public static <T> ApiResult<T> buildFailure(String errMsg) {
        ApiResult<T> result = new ApiResult<>();
        result.setSuccess(false);
        result.setErrMsg(errMsg);
        return result;
    }

    public static <T> ApiResult<T> buildFailure(int code, String errMsg) {
        ApiResult<T> result = new ApiResult<>();
        result.setSuccess(false);
        result.setCode(code);
        result.setErrMsg(errMsg);
        return result;
    }
}
