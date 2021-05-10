package com.cy.onepush.common.publishlanguage.console;

import com.cy.onepush.common.framework.publishedlanguage.AggregateId;

public class ConsoleId extends AggregateId<String> {

    public static ConsoleId of(String id) {
        return new ConsoleId(id);
    }

    protected ConsoleId(String id) {
        super(id);
    }
}
