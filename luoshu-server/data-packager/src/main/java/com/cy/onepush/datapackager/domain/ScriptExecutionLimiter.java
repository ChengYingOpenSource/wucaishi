package com.cy.onepush.datapackager.domain;

import com.cy.onepush.common.exception.LimitExceedException;
import com.cy.onepush.common.framework.domain.AbstractEntity;
import com.cy.onepush.debugtool.domain.DebugTool;
import com.cy.onepush.execution.context.domain.ExecutionContext;

import java.util.*;
import java.util.function.Consumer;

/**
 * script execution limiter
 *
 * @author WhatAKitty
 * @date 2021-2-7
 * @since 0.0.1
 */
public class ScriptExecutionLimiter extends AbstractEntity<String> {

    private List<LimitRule> limitRules;
    private LimitExceedStrategy limitExceedStrategy;

    public ScriptExecutionLimiter(LimitExceedStrategy limitExceedStrategy, LimitRule... limitRules) {
        this.setId(UUID.randomUUID().toString());
        this.limitRules = Arrays.asList(limitRules);
        this.limitExceedStrategy = limitExceedStrategy;
    }

    public ScriptExecutionEngine watch(ScriptExecutionEngine scriptExecutionEngine) {
        return new ScriptExecutionWatchableEngine(scriptExecutionEngine, this);
    }

    private void check(LimitResource limitResource) {
        boolean matched = limitRules.parallelStream().allMatch(item -> item.isMatch(limitResource));
        if (!matched) {
            throw new LimitExceedException(LimitExceedStrategy.REJECT.name());
        }
    }

    public List<LimitRule> getLimitRules() {
        return limitRules;
    }

    public LimitExceedStrategy getLimitExceedStrategy() {
        return limitExceedStrategy;
    }

    public void setLimitRules(List<LimitRule> limitRules) {
        this.limitRules = limitRules;
    }

    public void setLimitExceedStrategy(LimitExceedStrategy limitExceedStrategy) {
        this.limitExceedStrategy = limitExceedStrategy;
    }

    public static class ScriptExecutionWatchableEngine extends ScriptExecutionEngine {

        private final ScriptExecutionLimiter scriptExecutionLimiter;
        private final ScriptExecutionEngine delegate;

        public ScriptExecutionWatchableEngine(ScriptExecutionEngine delegate,
                                              ScriptExecutionLimiter scriptExecutionLimiter) {
            this.delegate = delegate;
            this.scriptExecutionLimiter = scriptExecutionLimiter;
        }

        @Override
        public String getType() {
            return this.delegate.getType();
        }

        @Override
        public void destroy() {
            this.delegate.destroy();
        }

        @Override
        protected void _init() {
            this.delegate._init();
        }

        @Override
        protected boolean _check(ExecutionContext context, ExecutionScript script) {
            return this.delegate._check(context, script);
        }

        @Override
        protected Object _execute(ExecutionContext context, ExecutionScript script, Map<String, Object> params, Map<String, Object> functions) {
            Map<String, Object> newFunctions = new HashMap<>(functions);
            newFunctions.put("limiter", (Consumer<LimitResource>) scriptExecutionLimiter::check);

            try {
                return this.delegate._execute(context, script, params, newFunctions);
            } catch (LimitExceedException e) {
                final LimitExceedStrategy strategy = LimitExceedStrategy.valueOf(e.getStrategy());
                switch (strategy) {
                    case REJECT:
                    default:
                        if (context.isDebug()) {
                            final DebugTool debugTool = context.getDebugTool();
                            debugTool.printError("the script has exceed limit");
                        }
                }

                throw e;
            }
        }

    }

}
