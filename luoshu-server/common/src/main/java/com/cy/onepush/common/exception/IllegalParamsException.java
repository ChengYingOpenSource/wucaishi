package com.cy.onepush.common.exception;

public class IllegalParamsException extends AbstractAppRuntimeException {

    public IllegalParamsException() {
        super();
    }

    public IllegalParamsException(String message, Object... params) {
        super(message, params);
    }

    public IllegalParamsException(String message, Throwable cause, Object... params) {
        super(message, cause, params);
    }

    public IllegalParamsException(Throwable cause) {
        super(cause);
    }

    protected IllegalParamsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object... params) {
        super(message, cause, enableSuppression, writableStackTrace, params);
    }
}
