package com.cy.onepush.datapackager.domain.event;

import com.cy.onepush.datapackager.domain.ScriptExecutionEngine;
import org.springframework.context.ApplicationEvent;

public class ScriptExecutionEngineInitedEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param scriptExecutionEngine the object on which the event initially occurred or with
     *                              which the event is associated (never {@code null})
     */
    public ScriptExecutionEngineInitedEvent(ScriptExecutionEngine scriptExecutionEngine) {
        super(scriptExecutionEngine);
    }

    @Override
    public ScriptExecutionEngine getSource() {
        return (ScriptExecutionEngine) super.getSource();
    }

}
