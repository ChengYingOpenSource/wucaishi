package com.cy.onepush.plugins.extension.datasource.jdbc;

import com.alibaba.druid.util.JdbcUtils;
import com.cy.onepush.common.exception.ResourceLimitException;
import com.cy.onepush.common.exception.ResourceNotFoundException;
import com.cy.onepush.datasource.domain.DataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public class JDBCUtils {

    private static final Integer MAX_CONNECTION_COUNT = 10;

    private static volatile int connectionTesting = 0;
    private static final Object LOCK = new Object();

    public static boolean validate(DataSourceProperties dataSourceProperties) {
        // open connection
        synchronized (LOCK) {
            int nowOpened = connectionTesting;
            if (nowOpened >= MAX_CONNECTION_COUNT) {
                // exceed
                final ResourceLimitException resourceLimitException = new ResourceLimitException("now opened exceed %d test connection, please try later", MAX_CONNECTION_COUNT);
                log.error(resourceLimitException.getMessage());
                throw resourceLimitException;
            }

            connectionTesting++;
        }

        final Properties properties = propertiesResolve(dataSourceProperties.toJdkProperties());

        // driver class load
        final String driverKlassName = properties.getProperty("driverClassName");
        if (StringUtils.isBlank(driverKlassName)) {
            // the class name is null
            return false;
        }
        try {
            Class.forName(driverKlassName);
        } catch (ClassNotFoundException e) {
            final ResourceNotFoundException resourceNotFoundException = new ResourceNotFoundException("the driver class named %s not found", driverKlassName);
            log.error(resourceNotFoundException.getMessage());
            throw resourceNotFoundException;
        }

        // try to connection test
        try (
            final Connection connection = DriverManager.getConnection(properties.getProperty("druid.url"), properties.getProperty("druid.username"), properties.getProperty("druid.password"));
        ) {
            // test sql execute
            connection.prepareCall(properties.getProperty("validationSql"));
            return true;
        } catch (SQLException t) {
            // failed
            return false;
        } finally {
            synchronized (LOCK) {
                connectionTesting--;
            }
        }
    }

    static Properties propertiesResolve(Properties properties) {
        Properties cloned = new Properties(properties);
        if (properties.containsKey("url")) {
            properties.setProperty("druid.url", properties.getProperty("url"));
        }
        if (properties.containsKey("username")) {
            properties.setProperty("druid.username", properties.getProperty("username"));
        }
        if (properties.containsKey("password")) {
            properties.setProperty("druid.password", properties.getProperty("password"));
        }
        if (!properties.containsKey("validationSql")) {
            properties.setProperty("validationSql", "select 1 from dual");
        }
        if (!properties.containsKey("driverClassName")) {
            try {
                String rawDriverClassName = JdbcUtils.getDriverClassName(properties.getProperty("druid.url"));
                properties.setProperty("driverClassName", rawDriverClassName);
            } catch (SQLException t) {
                // ignore
            }
        }
        return cloned;
    }

}
