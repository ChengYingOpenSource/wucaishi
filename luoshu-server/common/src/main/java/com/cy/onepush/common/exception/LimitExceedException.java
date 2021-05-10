package com.cy.onepush.common.exception;

import lombok.Getter;

@Getter
public class LimitExceedException extends AbstractAppRuntimeException {

    private final String strategy;

    public LimitExceedException(String strategy) {
        super("the resource limit exceed and will be executed with strategy %s", strategy);
        this.strategy = strategy;
    }

}
