package com.cy.onepush.common.exception;

/**
 * the state is illegal
 *
 * @author WhatAKitty
 * @date 2020-12-17
 * @since 0.1.0
 */
public class IllegalStateException extends AbstractAppRuntimeException {

    public IllegalStateException() {
        super();
    }

    public IllegalStateException(String message, Object... params) {
        super(message, params);
    }

    public IllegalStateException(String message, Throwable cause, Object... params) {
        super(message, cause, params);
    }

    public IllegalStateException(Throwable cause) {
        super(cause);
    }

    protected IllegalStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... params) {
        super(message, cause, enableSuppression, writableStackTrace, params);
    }
}
