package com.cy.onepush.datapackager.domain;

import com.cy.onepush.common.framework.domain.ValueObject;

public class ExecutionScript extends ValueObject {

    public static ExecutionScript of(String type, String content) {
        return new ExecutionScript(type, content);
    }

    private final String type;
    private final String content;

    private ExecutionScript(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

}
