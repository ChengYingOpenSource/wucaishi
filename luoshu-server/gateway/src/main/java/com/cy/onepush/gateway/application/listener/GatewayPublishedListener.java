package com.cy.onepush.gateway.application.listener;

import com.cy.onepush.gateway.domain.event.GatewayPublishedEvent;
import com.cy.onepush.gateway.infrastructure.handlermapping.GatewayHandlerMappingAdapter;
import com.cy.onepush.gateway.infrastructure.handlermapping.GatewayHandlerWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayPublishedListener implements ApplicationListener<GatewayPublishedEvent> {

    private final GatewayHandlerMappingAdapter gatewayHandlerMappingAdapter;

    @Override
    public void onApplicationEvent(GatewayPublishedEvent event) {
        gatewayHandlerMappingAdapter.register(new GatewayHandlerWrapper(event.getGateway()));
    }

}
