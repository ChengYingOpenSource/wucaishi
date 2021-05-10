package com.cy.onepush.datasource.domain;

import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DataSource Repository
 *
 * @author WhatAKitty
 * @version 0.1.0
 * @date 2020-10-26
 */
public interface DataSourceRepository {

    List<DataSource> allDataSources();

    DataSource getById(DataSourceId dataSourceId);

    List<DataSource> batchGetByIds(Collection<DataSourceId> dataSourceIds);

    Set<String> allDataSourceTypes();

    void add(DataSource dataSource);

    void update(DataSource dataSource);

    void addOrUpdate(DataSource dataSource);

    void batchAddOrUpdate(Collection<DataSource> dataSources);

    DataSource createTemp(String dataSourceType, Map<String, String> dataSourceProperties);

    void delete(DataSource dataSource);

}
