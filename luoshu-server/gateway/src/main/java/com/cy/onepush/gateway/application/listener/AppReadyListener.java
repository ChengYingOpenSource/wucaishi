package com.cy.onepush.gateway.application.listener;

import com.cy.onepush.common.event.AppReadyEvent;
import com.cy.onepush.gateway.infrastructure.handlermapping.GatewayHandlerMappingAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class AppReadyListener implements ApplicationListener<AppReadyEvent> {

    private final AppReadyEvent appReadyEvent;
    private final GatewayHandlerMappingAdapter gatewayHandlerMappingAdapter;

    public AppReadyListener(GatewayHandlerMappingAdapter gatewayHandlerMappingAdapter) {
        this.appReadyEvent = new AppReadyEvent(UUID.randomUUID());
        this.gatewayHandlerMappingAdapter = gatewayHandlerMappingAdapter;
    }

    @Override
    public synchronized void onApplicationEvent(AppReadyEvent event) {
        appReadyEvent.setDataPackagerReady(event.isDataPackagerReady() ? Boolean.TRUE : appReadyEvent.isDataPackagerReady());
        appReadyEvent.setDataViewReady(event.isDataViewReady() ? Boolean.TRUE : appReadyEvent.isDataViewReady());
        appReadyEvent.setDataSourceReady(event.isDataSourceReady() ? Boolean.TRUE : appReadyEvent.isDataSourceReady());

        log.info("application ready status {}", appReadyEvent);

        if (!appReadyEvent.isReady()) {
            return;
        }

        log.info("all ready {} and start to refresh gateway", appReadyEvent);

        // all ready
        // start to publish gateway
        gatewayHandlerMappingAdapter.init();
    }

}
