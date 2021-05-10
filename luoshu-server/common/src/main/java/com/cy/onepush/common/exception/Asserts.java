package com.cy.onepush.common.exception;

import java.util.Optional;

/**
 * assert condition
 *
 * @author WhatAKitty
 * @date 2020-12-12
 * @since 0.1.0
 */
public class Asserts {

    public static void assertNotNull(Object obj, String id) {
        if (obj == null) {
            throw notFoundFail(id);
        }
    }

    public static void assertOptional(Optional<?> optional, String id) {
        if (optional.isPresent()) {
            return;
        }

        throw notFoundFail(id);
    }

    public static ResourceNotFoundException notFoundFail(String id) {
        return new ResourceNotFoundException("the resource %s not found", id);
    }

    public static void assertState(Object actual, Object excepted) {
        if (!actual.equals(excepted)) {
            throw new IllegalStateException("the state is illegal actual %s, excepted %s", String.valueOf(actual), String.valueOf(excepted));
        }
    }

    public static void assertTrue(boolean expression, String msg, Object...params) {
        if (!expression) {
            throw new IllegalParamsException(msg, params);
        }
    }

    public static void assertFalse(boolean expression, String msg, Object...params) {
        if (expression) {
            throw new IllegalParamsException(msg, params);
        }
    }

    public static void assertDuplicate(String msg, Object...params) {
        throw new DuplicateResourceException(msg, params);
    }

    private Asserts() {
    }

}
