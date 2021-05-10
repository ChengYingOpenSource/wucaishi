package com.cy.onepush.dataview.application.listener;

import com.cy.onepush.common.event.AppReadyEvent;
import com.cy.onepush.common.utils.DomainEventUtils;
import com.cy.onepush.common.utils.SpringUtils;
import com.cy.onepush.dataview.domain.event.DataViewReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DataViewReadyListener implements ApplicationListener<DataViewReadyEvent> {

    @Override
    public void onApplicationEvent(DataViewReadyEvent event) {
        final AppReadyEvent appReadyEvent = new AppReadyEvent(event);
        appReadyEvent.setDataViewReady(true);

        DomainEventUtils.publishEvent(appReadyEvent);
    }

}
