package com.cy.onepush.datasource.domain.event;

import com.cy.onepush.datasource.domain.DataSource;
import org.springframework.context.ApplicationEvent;

public class DataSourceInitedEvent extends ApplicationEvent {

    public DataSourceInitedEvent(DataSource source) {
        super(source);
    }

    @Override
    public DataSource getSource() {
        return (DataSource) super.getSource();
    }
}
