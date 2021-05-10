package com.cy.onepush.datasource.domain;

import com.cy.onepush.common.framework.domain.AbstractAggregateRoot;
import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.datasource.domain.event.DataSourceDestroyedEvent;
import com.cy.onepush.datasource.domain.event.DataSourceInitedEvent;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * DataSource Aggregation Root
 *
 * @author WhatAKitty
 * @version 0.1.0
 * @date 2020-10-26
 */
@Slf4j
@Getter
public class DataSource extends AbstractAggregateRoot<String> {

    /**
     * the name of the datasource
     */
    private final String name;
    /**
     * the type of the datasource, fetch the datasource
     */
    private final String type;
    /**
     * the properties that used to init the datasource
     */
    private final DataSourceProperties properties;

    public DataSource(DataSourceId id, String name, String type, DataSourceProperties properties) {
        super(id);
        this.name = name;
        this.type = type;
        this.properties = properties;
    }

    @Override
    public DataSourceId getId() {
        return DataSourceId.of(super.getId().getId());
    }

    /**
     * validate the datasource properties
     *
     * @return {true} valid properties while {false} means bad.
     */
    public boolean validateProperties() {
        return false;
    }

    /**
     * init the datasource
     *
     * <p>
     * init this datasource, if and only if the datasource inited successfully, the {@link DataSourceInitedEvent} will
     * be published.
     * </p>
     *
     * @return {true} init successfully； {false} init failed
     */
    public final boolean init() {
        // check the properties is valid
        if (!validateProperties()) {
            log.error("failed to init the datasource {} named {} due to bad properties {}", this.getId(), this.getName(), this.properties);
            return false;
        }

        // check health
        if (!checkHealth()) {
            log.warn("check the datasource {} named {} health failed", this.getId(), this.getName());
            return false;
        }

        // start to init the datasource
        log.info("start to init the datasource {} named {}", this.getId(), this.getName());
        if (!_init()) {
            log.error("failed to init the datasource {} named {}", this.getId(), this.getName());
            return false;
        }

        // publish
        publishEvent(new DataSourceInitedEvent(this));
        log.info("init datasource {} finished", this.getId());
        return true;
    }

    /**
     * destroy the datasource
     *
     * <p>
     * before close the container, the {@link #destroy()} method will be triggered. If and only if destroy successfully
     * , the event {@link com.cy.onepush.datasource.domain.event.DataSourceDestroyedEvent} will be published.
     * </p>
     *
     * @return {true} destroy successfully； {false} destroy failed
     */
    public final boolean destroy() {
        if (!_destroy()) {
            log.error("failed to destroy the datasource {}", this.getClass().getName());
            return false;
        }

        // publish
        publishEvent(new DataSourceDestroyedEvent(this));
        return true;
    }

    /**
     * check the datasource health
     * <p>
     * return {true} green while {false} error
     */
    public boolean checkHealth() {
        return false;
    }

    protected boolean _init() {
        return false;
    }

    protected boolean _destroy() {
        return false;
    }

}
