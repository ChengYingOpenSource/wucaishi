package com.cy.onepush.common.exception;

/**
 * resource not found exception
 *
 * @author WhatAKitty
 * @date 2020-12-12
 * @since 0.1.0
 */
public class ResourceNotFoundException extends AbstractAppRuntimeException {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message, Object... params) {
        super(message, params);
    }

    public ResourceNotFoundException(String message, Throwable cause, Object... params) {
        super(message, cause, params);
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ResourceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... params) {
        super(message, cause, enableSuppression, writableStackTrace, params);
    }
}
