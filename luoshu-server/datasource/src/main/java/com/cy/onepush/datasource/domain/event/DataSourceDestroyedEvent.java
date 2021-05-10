package com.cy.onepush.datasource.domain.event;

import com.cy.onepush.datasource.domain.DataSource;
import org.springframework.context.ApplicationEvent;

public class DataSourceDestroyedEvent extends ApplicationEvent {

    public DataSourceDestroyedEvent(DataSource source) {
        super(source);
    }
}
