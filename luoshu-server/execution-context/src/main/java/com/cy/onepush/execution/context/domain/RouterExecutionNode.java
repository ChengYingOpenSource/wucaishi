package com.cy.onepush.execution.context.domain;

public class RouterExecutionNode extends ExecutionNode {

    private final String url;
    private final String body;

    public RouterExecutionNode(ExecutionNode executionNode, String url, String body) {
        super(executionNode);
        this.url = url;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public String getBody() {
        return body;
    }

}
