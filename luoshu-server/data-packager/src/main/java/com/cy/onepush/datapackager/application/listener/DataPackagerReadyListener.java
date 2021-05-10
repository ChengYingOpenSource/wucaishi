package com.cy.onepush.datapackager.application.listener;

import com.cy.onepush.common.event.AppReadyEvent;
import com.cy.onepush.common.utils.DomainEventUtils;
import com.cy.onepush.common.utils.SpringUtils;
import com.cy.onepush.datapackager.domain.event.DataPackagerReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DataPackagerReadyListener implements ApplicationListener<DataPackagerReadyEvent> {

    @Override
    public void onApplicationEvent(DataPackagerReadyEvent event) {
        final AppReadyEvent appReadyEvent = new AppReadyEvent(event);
        appReadyEvent.setDataPackagerReady(true);

        DomainEventUtils.publishEvent(appReadyEvent);
    }

}
