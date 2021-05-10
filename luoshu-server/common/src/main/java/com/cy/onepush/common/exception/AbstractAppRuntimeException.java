package com.cy.onepush.common.exception;

/**
 * abstract application runtime exception
 *
 * @author WhatAKitty
 * @date 2020-12-12
 * @since 0.1.0
 */
public class AbstractAppRuntimeException extends RuntimeException {

    public AbstractAppRuntimeException() {
        super();
    }

    public AbstractAppRuntimeException(String message, Object... params) {
        super(String.format(message, params));
    }

    public AbstractAppRuntimeException(String message, Throwable cause, Object... params) {
        super(String.format(message, params), cause);
    }

    public AbstractAppRuntimeException(Throwable cause) {
        super(cause);
    }

    protected AbstractAppRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... params) {
        super(String.format(message, params), cause, enableSuppression, writableStackTrace);
    }

}
