package com.cy.onepush.datapackager.domain;

import com.cy.onepush.common.framework.domain.ValueObject;
import com.cy.onepush.common.utils.DomainEventUtils;
import com.cy.onepush.datapackager.domain.event.ScriptExecutionEngineInitedEvent;
import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.debugtool.domain.DebugTool;
import com.cy.onepush.execution.context.domain.ExecutionContext;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * script execution engine
 *
 * @author WhatAKitty
 * @date 2020-12-13
 * @since 0.1.0
 */
@Slf4j
public abstract class ScriptExecutionEngine extends ValueObject {

    public abstract String getType();

    public final void init() {
        // TODO check validated
        _init();

        // publish event
        DomainEventUtils.publishEvent(new ScriptExecutionEngineInitedEvent(this));
    }

    public final Object execute(ExecutionContext context,
                                Map<String, DataView> dataViews,
                                ExecutionScript script,
                                Map<String, Object> params,
                                LimitRule... limitRules) {
        // check the script is ok
        if (!_check(context, script)) {
            log.warn("the script {} is illegal", script);
            return false;
        }

        // execute the script by specific script engine such as groovy engine or ecmascript engine
        final DebugToolProxy debugToolProxy = new DebugToolProxy(context);
        final DataViewExecutorProxy dataViewExecutorProxy = new DataViewExecutorProxy(dataViews, context);
        final ScriptExecutionEngine delegate = new ScriptExecutionLimiter(LimitExceedStrategy.REJECT, limitRules)
            .watch(this);
        return delegate._execute(context, script, params, ImmutableMap.of(
            "dataViewers", dataViewExecutorProxy,
            "debugTool", debugToolProxy
        ));
    }

    public abstract void destroy();

    protected abstract void _init();

    protected abstract boolean _check(ExecutionContext context, ExecutionScript script);

    protected abstract Object _execute(ExecutionContext context,
                                       ExecutionScript script,
                                       Map<String, Object> params,
                                       Map<String, Object> functions);

    /**
     * data view executor proxy
     *
     * @author WhatAKitty
     * @date 2020-12-13
     * @since 0.1.0
     */
    public static class DataViewExecutorProxy {

        private final Map<String, DataView> dataViews;
        private final ExecutionContext executionContext;

        public DataViewExecutorProxy(Map<String, DataView> dataViews,
                                     ExecutionContext executionContext) {
            this.dataViews = ImmutableMap.copyOf(dataViews);
            this.executionContext = executionContext;
        }

        public Object get(String alias) {
            return get(alias, new HashMap<>(0));
        }

        public Object get(String alias, Map<String, Object> params) {
            final DataView dataView = dataViews.get(alias);
            if (dataView == null) {
                // cannot found the data view
                if (executionContext.isDebug()) {
                    executionContext.getDebugTool().printError("failed to find the data view named %s", alias);
                }
                return null;
            }

            // wrap params
            executionContext.setParams(params);

            // execute data view with execution context
            dataView.execute(executionContext);

            // get actual data
            final Object result = executionContext.getResult();
            if (result instanceof Map) {
                return ((Map<?, ?>) result).get("data");
            }

            return result;
        }

    }

    /**
     * debug tool proxy
     *
     * @author WhatAKitty
     * @date 2020-12-13
     * @since 0.1.0
     */
    public static class DebugToolProxy {

        private final DebugTool debugTool;
        private final ExecutionContext executionContext;

        public DebugToolProxy(ExecutionContext executionContext) {
            this.debugTool = executionContext.getDebugTool();
            this.executionContext = executionContext;
        }

        public void println(String template, Object... params) {
            this.print(template + "\r\n", params);
        }

        public void print(String template, Object... params) {
            if (!this.executionContext.isDebug()) {
                return;
            }

            // 打印日志
            debugTool.printLog(String.format(template, params));
        }

        public void printError(String template, Object... params) {
            if (!this.executionContext.isDebug()) {
                return;
            }

            // 打印日志
            debugTool.printError(String.format(template, params));
        }

    }

}
