package com.cy.onepush.gateway.domain.event;

import com.cy.onepush.gateway.domain.Gateway;
import org.springframework.context.ApplicationEvent;

public class GatewayPublishedEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param gateway the object on which the event initially occurred or with
     *                which the event is associated (never {@code null})
     */
    public GatewayPublishedEvent(Gateway gateway) {
        super(gateway);
    }

    public Gateway getGateway() {
        return (Gateway) getSource();
    }

}
