package com.cy.onepush.plugins.extension.datasource.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.datasource.domain.DataSourceProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.stream.Stream;

@Slf4j
public class JDBCDataSource extends DataSource {

    private static final String DEFAULT_TEST = "select 1 from dual";

    @Getter
    private NamedParameterJdbcTemplate jdbcTemplate;

    public JDBCDataSource(DataSourceId id, String name, String type, DataSourceProperties properties) {
        super(id, name, type, properties);
    }

    @Override
    public boolean validateProperties() {
        final DataSourceProperties properties = getProperties();
        return Stream.of("url", "username", "password")
            .allMatch(properties::containsAndHaveValue);
    }

    @Override
    public boolean checkHealth() {
        return JDBCUtils.validate(getProperties());
    }

    @Override
    protected boolean _init() {
        try {
            final javax.sql.DataSource jdbcDataSource = createJdbcDataSource();
            this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcDataSource);

            // init jdbc
            final DruidDataSource dataSource = (DruidDataSource) jdbcDataSource;
            log.debug("pre init datasource {}", this.getId());
            dataSource.init();

            log.info("the datasource {} inited", this.getId());
            return true;
        } catch (Exception e) {
            log.error("failed init the datasource {}", this.getId());
            return false;
        }
    }

    @Override
    protected boolean _destroy() {
        final DruidDataSource dataSource = (DruidDataSource) getJdbcTemplate().getJdbcTemplate().getDataSource();
        if (dataSource != null) {
            dataSource.close();
        }
        log.info("the datasource {} destroyed", this.getId());
        return true;
    }

    private javax.sql.DataSource createJdbcDataSource() {
        final DataSourceProperties properties = getProperties();

        // create a datasource from datasource properties
        final DruidDataSource jdbcDataSource = new DruidDataSource();
        jdbcDataSource.configFromPropety(JDBCUtils.propertiesResolve(properties.toJdkProperties()));
        return jdbcDataSource;
    }


}
