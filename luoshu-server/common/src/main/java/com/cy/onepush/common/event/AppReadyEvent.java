package com.cy.onepush.common.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@ToString
@EqualsAndHashCode(callSuper = true)
public class AppReadyEvent extends ApplicationEvent {

    private boolean dataSourceReady;
    private boolean dataViewReady;
    private boolean dataPackagerReady;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public AppReadyEvent(Object source) {
        super(source);
    }

    public boolean isDataSourceReady() {
        return dataSourceReady;
    }

    public void setDataSourceReady(boolean dataSourceReady) {
        this.dataSourceReady = dataSourceReady;
    }

    public boolean isDataViewReady() {
        return dataViewReady;
    }

    public void setDataViewReady(boolean dataViewReady) {
        this.dataViewReady = dataViewReady;
    }

    public boolean isDataPackagerReady() {
        return dataPackagerReady;
    }

    public void setDataPackagerReady(boolean dataPackagerReady) {
        this.dataPackagerReady = dataPackagerReady;
    }

    public boolean isReady() {
        return dataPackagerReady && dataViewReady && dataSourceReady;
    }

}
