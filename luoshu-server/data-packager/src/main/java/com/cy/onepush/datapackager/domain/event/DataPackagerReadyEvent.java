package com.cy.onepush.datapackager.domain.event;

import org.springframework.context.ApplicationEvent;

public class DataPackagerReadyEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public DataPackagerReadyEvent(Object source) {
        super(source);
    }
}
