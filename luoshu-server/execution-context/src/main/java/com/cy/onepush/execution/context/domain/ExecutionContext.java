package com.cy.onepush.execution.context.domain;

import com.cy.onepush.common.framework.domain.AbstractAggregateRoot;
import com.cy.onepush.common.publishlanguage.executioncontext.ExecutionContextId;
import com.cy.onepush.debugtool.domain.DebugTool;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ExecutionContext extends AbstractAggregateRoot<String> {

    /**
     * the invocation tree
     */
    private final ExecutionNode head;
    /**
     * the params of the current invocation
     */
    @Getter
    @Setter
    private Map<String, Object> params;
    /**
     * the result of the current invocation
     */
    @Getter
    @Setter
    private Object result;
    /**
     * is debug context
     */
    private final boolean debug;
    /**
     * debug tool
     */
    @Getter
    private final DebugTool debugTool;

    /**
     * the current node
     */
    private ExecutionNode currentNode;

    public ExecutionContext(ExecutionContextId id) {
        this(id, false, null);
    }

    public ExecutionContext(ExecutionContextId id, boolean debug, DebugTool debugTool) {
        super(id);
        this.head = new ExecutionNode(null);
        this.currentNode = this.head;
        this.debug = debug;
        this.debugTool = debugTool;
    }

    @Override
    public ExecutionContextId getId() {
        return ExecutionContextId.of(super.getId().getId());
    }

    public boolean isDebug() {
        return debug;
    }

    public void enterNode() {
        // create a generic node
        final ExecutionNode node = new ExecutionNode(currentNode);

        addNode(node);
    }

    public void enterRouterNode(String url, String body) {
        final RouterExecutionNode routerExecutionNode = new RouterExecutionNode(currentNode, url, body);

        addNode(routerExecutionNode);
    }

    public void enterDataNode(Object params) {
        final DataExecutionNode dataExecutionNode = new DataExecutionNode(currentNode, params);

        addNode(dataExecutionNode);
    }

    private void addNode(ExecutionNode node) {
        if (isDebug()) {
            debugTool.printLog("enter the execution node %s", node.getClass().getSimpleName());
        }

        // append to the current node tree
        currentNode.append(node);
        currentNode = node;

        // enter the node
        currentNode.enter();
    }

    public void leaveNode() {
        // exit the current node execution context
        currentNode.exit();

        if (isDebug()) {
            debugTool.printLog("leave the execution node %s with execution time %d ms",
                currentNode.getClass().getSimpleName(), currentNode.executionTime(TimeUnit.MICROSECONDS));
        }

        // get the parent node
        currentNode = currentNode.getParent();
    }

    public long executionTime(TimeUnit timeUnit) {
        return head.executionTime(timeUnit);
    }

    public void enterContext() {
        currentNode.enter();
    }

    public void exitContext() {
        currentNode.exit();
    }

    public ExecutionTreeView executionTreeView() {
        throw new UnsupportedOperationException();
    }

    private long bfs(ExecutionNode root, TimeUnit timeUnit) {
        final Queue<ExecutionNode> queue = new LinkedList<>();
        queue.add(root);

        long allExecutionTime = 0;
        while (!queue.isEmpty()) {
            final int size = queue.size();

            long leafMaxExecutionTime = 0;
            for (int i = 0; i < size; i++) {
                final ExecutionNode current = queue.poll();
                assert current != null;

                final long executionTime = current.executionTime(timeUnit);

                leafMaxExecutionTime = Math.max(leafMaxExecutionTime, executionTime);

                final List<ExecutionNode> children = current.getChildren();
                queue.addAll(children);
            }

            allExecutionTime += leafMaxExecutionTime;
        }

        return allExecutionTime;
    }

}
