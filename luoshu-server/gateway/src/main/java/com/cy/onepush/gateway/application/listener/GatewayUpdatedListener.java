package com.cy.onepush.gateway.application.listener;

import com.cy.onepush.common.publishlanguage.gateway.GatewayId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.gateway.domain.Gateway;
import com.cy.onepush.gateway.domain.GatewayIdWithVersion;
import com.cy.onepush.gateway.domain.GatewayRepository;
import com.cy.onepush.gateway.domain.event.GatewayUpdatedEvent;
import com.cy.onepush.gateway.infrastructure.handlermapping.GatewayHandlerMappingAdapter;
import com.cy.onepush.gateway.infrastructure.handlermapping.GatewayHandlerWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GatewayUpdatedListener implements ApplicationListener<GatewayUpdatedEvent> {

    private final GatewayHandlerMappingAdapter gatewayHandlerMappingAdapter;

    private final GatewayRepository gatewayRepository;

    @Override
    public void onApplicationEvent(GatewayUpdatedEvent event) {
        final Gateway updatedGateway = event.getSource();

        final Gateway gateway = gatewayRepository.get(updatedGateway.getIdWithVersion());

        gatewayHandlerMappingAdapter.refresh(new GatewayHandlerWrapper(gateway));
    }

}
