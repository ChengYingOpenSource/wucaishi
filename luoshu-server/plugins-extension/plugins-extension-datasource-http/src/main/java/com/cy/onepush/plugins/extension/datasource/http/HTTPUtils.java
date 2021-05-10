package com.cy.onepush.plugins.extension.datasource.http;

import com.cy.onepush.common.exception.ResourceLimitException;
import com.cy.onepush.datasource.domain.DataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.AsyncClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class HTTPUtils {

    private static final Integer MAX_CONNECTION_COUNT = 10;

    private static volatile int connectionTesting = 0;
    private static final Object LOCK = new Object();

    static boolean validate(DataSourceProperties dataSourceProperties) {
        // open connection
        synchronized (LOCK) {
            final int nowOpened = connectionTesting;
            if (nowOpened > MAX_CONNECTION_COUNT) {
                // exceed
                final ResourceLimitException resourceLimitException = new ResourceLimitException("now opened exceed %d test connection, please try later", MAX_CONNECTION_COUNT);
                log.error(resourceLimitException.getMessage());
                throw resourceLimitException;
            }

            connectionTesting++;
        }

        final Properties properties = propertiesResolved(dataSourceProperties.toJdkProperties());

        final SimpleClientHttpRequestFactory clientFactory = new SimpleClientHttpRequestFactory();
        clientFactory.setTaskExecutor(new SimpleAsyncTaskExecutor("test-http-connection-"));
        try {
            final AsyncClientHttpRequest request = clientFactory.createAsyncRequest(URI.create(properties.getProperty("url")), HttpMethod.GET);
            final ListenableFuture<ClientHttpResponse> listenableFuture = request.executeAsync();

            for (int i = 0; i < 3; i++) {
                final ClientHttpResponse response = listenableFuture.get(1, TimeUnit.SECONDS);
                if (response.getRawStatusCode() == 200) {
                    return true;
                }
            }

            log.warn("the http datasource connection test failed with time exceed or the service not ready");
            return false;
        } catch (InterruptedException e) {
            log.warn("the http datasource interrupted");
            return false;
        } catch (IOException | ExecutionException | TimeoutException e) {
            log.warn("the http datasource connection test failed with", e);
            return false;
        } finally {
            synchronized (LOCK) {
                connectionTesting--;
            }
        }
    }

    static Properties propertiesResolved(Properties raw) {
        Properties properties = new Properties(raw);
        if (!properties.containsKey("protocol")) {
            properties.setProperty("protocol", "http");
        }
        if (!properties.containsKey("url")) {
            properties.setProperty("url", createUrl(properties));
        }

        return properties;
    }

    static String createUrl(Properties properties) {
        final String protocol = properties.getProperty("protocol");
        final String host = properties.getProperty("host");
        final String port = properties.getProperty("port");
        return String.format("%s://%s:%s", protocol, host, port);
    }

}
