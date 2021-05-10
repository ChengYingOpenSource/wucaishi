package com.cy.onepush.plugins.extension.datasource.http;

import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.common.publishlanguage.executioncontext.ExecutionContextId;
import com.cy.onepush.common.utils.DomainEventUtils;
import com.cy.onepush.datasource.domain.DataSourceProperties;
import com.cy.onepush.datasource.domain.DataSourceProperty;
import com.cy.onepush.execution.context.domain.ExecutionContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DomainEventUtils.class)
@PowerMockIgnore({"javax.net.ssl.*"})
public class HTTPDataViewTest {

    private HTTPDataSource dataSource;

    @Before
    public void before() {
        PowerMockito.mockStatic(DomainEventUtils.class);
        PowerMockito.doNothing().when(DomainEventUtils.class);
        DomainEventUtils.publishEvent(null);

        DataSourceProperties dataSourceProperties = new DataSourceProperties(ImmutableMap.of(
            "host", DataSourceProperty.of("host", "baidu.com"),
            "port", DataSourceProperty.of("port", "80")
        ));

        this.dataSource = new HTTPDataSource(DataSourceId.of("test"), "test", "HTTP", dataSourceProperties);
        final boolean init = dataSource.init();

        Assert.assertTrue(init);
    }

    @Test
    public void test_normal() {
        HTTPDataView httpDataView = new HTTPDataView(DataViewId.of("test"), "test");
        httpDataView.bindDataSource(dataSource);
        httpDataView.setParams(ImmutableMap.of(
            "path", "/",
            "method", "GET"
        ));

        ExecutionContext context = new ExecutionContext(ExecutionContextId.of("test"));

        httpDataView.execute(context);

        Assert.assertTrue(true);
    }

    @Test
    public void test_json_object_parsed() {
        ObjectMapper objectMapper = new ObjectMapper();
        String mockResultData;
        try {
            mockResultData = objectMapper.writeValueAsString(ImmutableMap.of(
                "a", 1,
                "b", 2
            ));
        } catch (JsonProcessingException e) {
            Assert.fail();
            return;
        }

        ResponseEntity<String> mockResult = new ResponseEntity<>(mockResultData, HttpStatus.OK);

        final RestTemplate mock = PowerMockito.mock(RestTemplate.class);
        PowerMockito.when(mock.execute(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(mockResult);

        MockDataSource mockDataSource = new MockDataSource();
        mockDataSource.setRestTemplate(mock);

        HTTPDataView httpDataView = new HTTPDataView(DataViewId.of("test"), "test");
        httpDataView.bindDataSource(mockDataSource);
        httpDataView.setParams(ImmutableMap.of(
            "path", "/",
            "method", "GET"
        ));

        ExecutionContext context = new ExecutionContext(ExecutionContextId.of("test"));

        httpDataView.execute(context);

        final Object result = context.getResult();

        Assert.assertTrue(result instanceof Map);
        Assert.assertEquals(1, ((Map<?, ?>) result).get("a"));
        Assert.assertEquals(2, ((Map<?, ?>) result).get("b"));
    }

    @Test
    public void test_json_array_parsed() {
        ObjectMapper objectMapper = new ObjectMapper();
        String mockResultData;
        try {
            mockResultData = objectMapper.writeValueAsString(Lists.newArrayList(ImmutableMap.of(
                "a", 1,
                "b", 2
            )));
        } catch (JsonProcessingException e) {
            Assert.fail();
            return;
        }

        ResponseEntity<String> mockResult = new ResponseEntity<>(mockResultData, HttpStatus.OK);

        final RestTemplate mock = PowerMockito.mock(RestTemplate.class);
        PowerMockito.when(mock.execute(Mockito.anyString(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(mockResult);

        MockDataSource mockDataSource = new MockDataSource();
        mockDataSource.setRestTemplate(mock);

        HTTPDataView httpDataView = new HTTPDataView(DataViewId.of("test"), "test");
        httpDataView.bindDataSource(mockDataSource);
        httpDataView.setParams(ImmutableMap.of(
            "path", "/",
            "method", "GET"
        ));

        ExecutionContext context = new ExecutionContext(ExecutionContextId.of("test"));

        httpDataView.execute(context);

        final Object result = context.getResult();

        Assert.assertTrue(result instanceof List);
        Assert.assertEquals(1, ((List<?>) result).size());
        Assert.assertEquals(1, ((Map<?, ?>) ((List<?>) result).get(0)).get("a"));
        Assert.assertEquals(2, ((Map<?, ?>) ((List<?>) result).get(0)).get("b"));
    }

    private static class MockDataSource extends HTTPDataSource {

        @Getter
        @Setter
        private RestTemplate restTemplate;

        public MockDataSource() {
            super(DataSourceId.of(""), "", "HTTP", new DataSourceProperties(new HashMap<>()));
        }

    }

}