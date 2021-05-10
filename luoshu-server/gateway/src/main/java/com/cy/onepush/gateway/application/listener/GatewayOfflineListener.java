package com.cy.onepush.gateway.application.listener;

import com.cy.onepush.gateway.domain.Gateway;
import com.cy.onepush.gateway.domain.event.GatewayOfflineEvent;
import com.cy.onepush.gateway.infrastructure.handlermapping.GatewayHandlerMappingAdapter;
import com.cy.onepush.gateway.infrastructure.handlermapping.GatewayHandlerWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GatewayOfflineListener implements ApplicationListener<GatewayOfflineEvent> {

    private final GatewayHandlerMappingAdapter gatewayHandlerMappingAdapter;

    @Override
    public void onApplicationEvent(GatewayOfflineEvent event) {
        final Gateway gateway = event.getGateway();
        gatewayHandlerMappingAdapter.unregister(new GatewayHandlerWrapper(gateway));
    }

}
