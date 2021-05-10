package com.cy.onepush.plugins.extension.datasource.http;

import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.common.utils.DomainEventUtils;
import com.cy.onepush.datasource.domain.DataSourceProperties;
import com.cy.onepush.datasource.domain.DataSourceProperty;
import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DomainEventUtils.class)
@PowerMockIgnore({"javax.net.ssl.*"})
public class HTTPDataSourceTest {

    @Before
    public void before() {
        PowerMockito.mockStatic(DomainEventUtils.class);
        PowerMockito.doNothing().when(DomainEventUtils.class);
        DomainEventUtils.publishEvent(null);

    }

    @Test
    public void test_normal() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties(ImmutableMap.of(
            "host", DataSourceProperty.of("host", "baidu.com"),
            "port", DataSourceProperty.of("port", "80")
        ));

        HTTPDataSource httpDataSource = new HTTPDataSource(DataSourceId.of("test"), "test", "HTTP", dataSourceProperties);
        final boolean init = httpDataSource.init();

        Assert.assertTrue(init);
    }

    @Test
    public void test_check_health() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties(ImmutableMap.of(
            "host", DataSourceProperty.of("host", "baidu.com"),
            "port", DataSourceProperty.of("port", "80")
        ));

        HTTPDataSource httpDataSource = new HTTPDataSource(DataSourceId.of("test"), "test", "HTTP", dataSourceProperties);
        final boolean init = httpDataSource.checkHealth();

        Assert.assertTrue(init);
    }


}