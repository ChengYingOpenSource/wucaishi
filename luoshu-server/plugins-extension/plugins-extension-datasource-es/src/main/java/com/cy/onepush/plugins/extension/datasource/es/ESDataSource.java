package com.cy.onepush.plugins.extension.datasource.es;

import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.datasource.domain.DataSourceProperties;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

@Slf4j
public class ESDataSource extends DataSource {

    private static final Integer DEFAULT_CONNECTION_REQUEST_TIMEOUT = 10000; // 1s
    private static final Integer DEFAULT_CONNECT_TIMEOUT = 10000; // 1s
    private static final Integer DEFAULT_READ_TIMEOUT = 50000; // 5s

    @Getter
    private OkHttp3ClientHttpRequestFactory clientHttpRequestFactory;

    @Getter
    private RestTemplate restTemplate;

    public ESDataSource(DataSourceId id, String name, DataSourceProperties properties) {
        super(id, name, "HTTP", properties);
    }

    @Override
    public boolean validateProperties() {
        final DataSourceProperties properties = getProperties();
        return Stream.of("host", "port")
            .allMatch(properties::containsAndHaveValue);
    }

    @Override
    public boolean checkHealth() {
        return ESUtils.validate(getProperties());
    }

    @Override
    protected boolean _init() {
        try {
            this.restTemplate = createHttpTemplate(getProperties());

            log.info("the datasource {} inited", this.getId());
            return true;
        } catch (Exception e) {
            log.error("failed init the datasource {}", this.getId(), e);
            return false;
        }
    }

    @Override
    protected boolean _destroy() {
        final ClientHttpRequestFactory requestFactory = this.restTemplate.getRequestFactory();
        if (requestFactory instanceof HttpComponentsClientHttpRequestFactory) {
            try {
                ((HttpComponentsClientHttpRequestFactory) requestFactory).destroy();
            } catch (Exception e) {
                log.error("failed to destroy the http client due to", e);
                return false;
            }
        }

        log.info("the datasource {} destroyed", this.getId());
        return true;
    }

    private RestTemplate createHttpTemplate(DataSourceProperties dataSourceProperties) {
        final ClassLoader classLoader = this.getClass().getClassLoader();

        try {
            final Class<?> klass = Class.forName("org.springframework.http.client.OkHttp3ClientHttpRequestFactory", true, classLoader);
            this.clientHttpRequestFactory = (OkHttp3ClientHttpRequestFactory) klass.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // never happen
            throw new Error("failed to load plugin");
        }

        clientHttpRequestFactory.setConnectTimeout(dataSourceProperties.getIntOrDefault("connectTimeout", DEFAULT_CONNECT_TIMEOUT));
        clientHttpRequestFactory.setReadTimeout(dataSourceProperties.getIntOrDefault("readTimeout", DEFAULT_READ_TIMEOUT));
        clientHttpRequestFactory.setWriteTimeout(dataSourceProperties.getIntOrDefault("writeTimeout", DEFAULT_CONNECTION_REQUEST_TIMEOUT));

        return new RestTemplate(clientHttpRequestFactory);
    }

}
