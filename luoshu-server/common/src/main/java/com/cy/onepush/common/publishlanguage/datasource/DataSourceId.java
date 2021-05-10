package com.cy.onepush.common.publishlanguage.datasource;

import com.cy.onepush.common.framework.publishedlanguage.AggregateId;

/**
 * 数据源ID
 *
 * @author WhatAKitty
 * @date 2020-10-26
 * @version 0.1.0
 */
public class DataSourceId extends AggregateId<String> {

    public static DataSourceId of(String id) {
        return new DataSourceId(id);
    }

    protected DataSourceId(String id) {
        super(id);
    }

}
