package com.cy.onepush.common.framework.infrastructure.web;

/**
 * result code
 *
 * @author WhatAKitty
 * @date 2020-12-16
 * @since 0.1.0
 */
public enum ResultCode {

    /**
     * success
     */
    SUCCESS(0),
    /**
     * system error
     */
    ERROR(-1),
    /**
     * bad request
     */
    BAD_REQUEST(-400),
    /**
     * not found
     */
    NOT_FOUND(-404);

    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
