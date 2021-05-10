package com.cy.onepush.plugins.extension.datasource.jdbc;

import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.datasource.domain.DataSourceProperties;

import java.util.HashMap;

public class MockDataSource extends DataSource {


    public MockDataSource() {
        super(DataSourceId.of("mock"), "mock", "mock", new DataSourceProperties(new HashMap<>(0)));
    }

    @Override
    public boolean validateProperties() {
        return true;
    }

    @Override
    public boolean checkHealth() {
        return true;
    }

    @Override
    protected boolean _init() {
        return true;
    }

    @Override
    protected boolean _destroy() {
        return true;
    }
}
