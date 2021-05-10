package com.cy.onepush.common.publishlanguage.debugtool;

import com.cy.onepush.common.framework.publishedlanguage.AggregateId;

public class DebugToolId extends AggregateId<String> {

    public static DebugToolId of(String id) {
        return new DebugToolId(id);
    }

    protected DebugToolId(String id) {
        super(id);
    }
}
