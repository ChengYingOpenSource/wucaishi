package com.cy.onepush.gateway.domain.event;

import com.cy.onepush.gateway.domain.Gateway;
import org.springframework.context.ApplicationEvent;

public class GatewayUpdatedEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public GatewayUpdatedEvent(Gateway source) {
        super(source);
    }

    public Gateway getSource() {
        return (Gateway) super.getSource();
    }

}
