package com.cy.onepush.execution.context.domain;

public class DataExecutionNode extends ExecutionNode {

    private final Object params;
    private Object result;

    public DataExecutionNode(ExecutionNode executionNode, Object params) {
        super(executionNode);
        this.params = params;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Object getParams() {
        return params;
    }

    public Object getResult() {
        return result;
    }
}
