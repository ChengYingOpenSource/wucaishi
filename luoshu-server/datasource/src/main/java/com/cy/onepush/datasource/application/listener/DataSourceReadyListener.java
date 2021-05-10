package com.cy.onepush.datasource.application.listener;

import com.cy.onepush.common.event.AppReadyEvent;
import com.cy.onepush.common.utils.DomainEventUtils;
import com.cy.onepush.common.utils.SpringUtils;
import com.cy.onepush.datasource.domain.event.DataSourceReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DataSourceReadyListener implements ApplicationListener<DataSourceReadyEvent> {

    @Override
    public void onApplicationEvent(DataSourceReadyEvent event) {
        final AppReadyEvent appReadyEvent = new AppReadyEvent(event);
        appReadyEvent.setDataSourceReady(true);

        DomainEventUtils.publishEvent(appReadyEvent);
    }
}
