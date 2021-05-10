package com.cy.onepush.execution.context.domain;

import com.cy.onepush.common.framework.domain.AbstractEntity;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
public class ExecutionNode extends AbstractEntity<String> {

    private long enterTime;
    private long leaveTime;

    private final ExecutionNode parent;
    private final List<ExecutionNode> children;

    public ExecutionNode(ExecutionNode parent) {
        this.enterTime = this.leaveTime = 0L;
        this.parent = parent;
        this.children = new ArrayList<>(8);
    }

    public ExecutionNode append(ExecutionNode node) {
        children.add(node);
        return this;
    }

    public long executionTime(TimeUnit timeUnit) {
        return leaveTime - enterTime;
    }

    public void enter() {
        this.enterTime = System.currentTimeMillis();
    }

    public void exit() {
        this.leaveTime = System.currentTimeMillis();
    }


}
