package com.cy.onepush.debugtool.domain;

import com.cy.onepush.common.framework.domain.ValueObject;

public class MonitorVariable extends ValueObject {

    public static MonitorVariable of(String name, String condition) {
        return new MonitorVariable(name, condition);
    }

    private final String name;
    private final String condition;

    private MonitorVariable(String name, String condition) {
        this.name = name;
        this.condition = condition;
    }

}
