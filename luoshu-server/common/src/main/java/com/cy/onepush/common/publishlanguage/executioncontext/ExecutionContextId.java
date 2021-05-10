package com.cy.onepush.common.publishlanguage.executioncontext;

import com.cy.onepush.common.framework.publishedlanguage.AggregateId;

public class ExecutionContextId extends AggregateId<String> {

    public static ExecutionContextId of(String id) {
        return new ExecutionContextId(id);
    }

    protected ExecutionContextId(String id) {
        super(id);
    }
}
