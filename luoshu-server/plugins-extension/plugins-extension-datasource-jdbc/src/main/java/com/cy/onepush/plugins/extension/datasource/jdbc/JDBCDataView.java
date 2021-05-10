package com.cy.onepush.plugins.extension.datasource.jdbc;

import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.dataview.domain.CommandDataView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

@Slf4j
public class JDBCDataView extends CommandDataView {

    public JDBCDataView(DataViewId id, String name) {
        super(id, name);
    }

    public JDBCDataView(DataViewId id, String name, Version version) {
        super(id, name, version);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Object _execute(DataSource dataSource, Object params) {
        assert dataSource instanceof JDBCDataSource;

        // params pre handle
        if (params != null && !(params instanceof Map)) {
            throw new IllegalArgumentException();
        }

        final JDBCDataSource jdbcDataSource = (JDBCDataSource) dataSource;

        final NamedParameterJdbcTemplate jdbcTemplate = jdbcDataSource.getJdbcTemplate();
        return jdbcTemplate.queryForList(getCommand(), (Map<String, Object>) params);
    }

}
