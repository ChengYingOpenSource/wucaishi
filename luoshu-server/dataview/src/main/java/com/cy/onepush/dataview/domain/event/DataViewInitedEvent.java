package com.cy.onepush.dataview.domain.event;

import com.cy.onepush.dataview.domain.DataView;
import org.springframework.context.ApplicationEvent;

public class DataViewInitedEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param dataView the object on which the event initially occurred or with
     *                 which the event is associated (never {@code null})
     */
    public DataViewInitedEvent(DataView dataView) {
        super(dataView);
    }

    @Override
    public DataView getSource() {
        return (DataView) super.getSource();
    }
}
