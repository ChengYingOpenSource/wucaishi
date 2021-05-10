package com.cy.onepush.datasource.domain.event;

import org.springframework.context.ApplicationEvent;

public class DataSourceReadyEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public DataSourceReadyEvent(Object source) {
        super(source);
    }

}
