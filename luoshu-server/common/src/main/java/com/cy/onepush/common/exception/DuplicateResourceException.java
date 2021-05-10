package com.cy.onepush.common.exception;

public class DuplicateResourceException extends AbstractAppRuntimeException {

    public DuplicateResourceException() {
        super();
    }

    public DuplicateResourceException(String message, Object... params) {
        super(message, params);
    }

    public DuplicateResourceException(String message, Throwable cause, Object... params) {
        super(message, cause, params);
    }

    public DuplicateResourceException(Throwable cause) {
        super(cause);
    }

    protected DuplicateResourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... params) {
        super(message, cause, enableSuppression, writableStackTrace, params);
    }
}
