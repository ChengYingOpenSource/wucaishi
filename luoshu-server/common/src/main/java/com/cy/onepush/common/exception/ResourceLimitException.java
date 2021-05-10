package com.cy.onepush.common.exception;

public class ResourceLimitException extends AbstractAppRuntimeException {

    public ResourceLimitException() {
        super();
    }

    public ResourceLimitException(String message, Object... params) {
        super(message, params);
    }

    public ResourceLimitException(String message, Throwable cause, Object... params) {
        super(message, cause, params);
    }

    public ResourceLimitException(Throwable cause) {
        super(cause);
    }

    protected ResourceLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... params) {
        super(message, cause, enableSuppression, writableStackTrace, params);
    }

}
