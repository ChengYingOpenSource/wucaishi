package com.cy.onepush.common.framework.infrastructure.web;

import lombok.Data;

import java.util.List;

/**
 * result of rest api
 *
 * @param <T> the data type
 */
public class Result<T> {

    public static <T> Builder<T> builder() {
        return new Result.Builder<>();
    }

    private final int code;
    private final T data;
    private final String msg;

    protected Result(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode() == code;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public static class Builder<T> {

        protected int code;
        protected T data;
        protected String msg;

        public Builder<T> code(int code) {
            this.code = code;
            return this;
        }

        public Builder<T> data(T data) {
            this.data = data;
            return this;
        }

        public Builder<T> msg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder<T> success() {
            return this.success(null);
        }

        public Builder<T> success(T data) {
            this.code = ResultCode.SUCCESS.getCode();
            this.data = data;
            return this;
        }

        public Builder<T> failed(ResultCode resultCode) {
            return this.failed(resultCode.getCode());
        }

        public Builder<T> failed(int code) {
            return this.failed(code, null);
        }

        public Builder<T> failed(ResultCode resultCode, String msg) {
            return this.failed(resultCode.getCode(), msg);
        }

        public Builder<T> failed(int code, String msg) {
            this.code = code;
            this.data = null;
            this.msg = msg;
            return this;
        }

        public Result<T> build() {
            return new Result<>(this.code, this.data, this.msg);
        }

    }

}
