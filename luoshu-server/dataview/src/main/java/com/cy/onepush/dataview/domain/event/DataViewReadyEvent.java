package com.cy.onepush.dataview.domain.event;

import org.springframework.context.ApplicationEvent;

public class DataViewReadyEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public DataViewReadyEvent(Object source) {
        super(source);
    }
}
