package com.cy.onepush.debugtool.domain;

import com.cy.onepush.common.framework.domain.AbstractAggregateRoot;
import com.cy.onepush.common.publishlanguage.debugtool.DebugToolId;
import com.cy.onepush.console.domain.Console;

import java.util.Set;

public class DebugTool extends AbstractAggregateRoot<String> {

    private final Console console;
    /**
     * 监测变量列表
     */
    private Set<MonitorVariable> monitorVariables;

    public DebugTool(DebugToolId id, Console console) {
        super(id);
        this.console = console;
    }

    @Override
    public DebugToolId getId() {
        return DebugToolId.of(super.getId().getId());
    }

    public void addWatch(String variableName) {
        // 监测变量有效性：存在等
        // 添加
    }

    public void removeWatch(String variableName) {

    }

    public void printLog(String log, Object...params) {
        this.console.addLog("info", String.format(log, params));
    }

    public void printError(String log, Object...params) {
        this.console.addLog("error", String.format(log, params));
    }

    public void printError(Throwable t, String log, Object...params) {
        final StringBuilder sb = new StringBuilder(String.format(log, params));
        sb.append("\r\nstack trace :\r\n");
        final StackTraceElement[] stackTrace = t.getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            sb.append("at ").append(stackTraceElement.toString()).append("\r\n");
        }

        printError(sb.toString());
    }

}
