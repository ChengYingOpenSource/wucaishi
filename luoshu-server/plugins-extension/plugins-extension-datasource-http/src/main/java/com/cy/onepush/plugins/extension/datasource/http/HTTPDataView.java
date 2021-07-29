package com.cy.onepush.plugins.extension.datasource.http;

import com.cy.onepush.common.exception.Asserts;
import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.dataview.domain.ParamsDataView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class HTTPDataView extends ParamsDataView {

    private static final ObjectMapper objectMapper;

    private static final boolean romePresent;

    private static final boolean jaxb2Present;

    private static final boolean jackson2Present;

    private static final boolean jackson2XmlPresent;

    private static final boolean jackson2SmilePresent;

    private static final boolean jackson2CborPresent;

    private static final boolean gsonPresent;

    private static final boolean jsonbPresent;

    public HTTPDataView(DataViewId id, String name) {
        super(id, name);
    }

    public HTTPDataView(DataViewId id, String name, Version version) {
        super(id, name, version);
    }

    @Override
    protected boolean validateParams() {
        final Map<String, Object> params = getParams();
        Asserts.assertTrue(params.containsKey("contextUrl"), "the context is empty");
        Asserts.assertTrue(params.containsKey("method"), "the method is empty");
        return true;
    }

    @Override
    protected Object _execute(DataSource dataSource, Object raw) {
        assert dataSource instanceof HTTPDataSource;

        // params pre handle
        if (raw != null && !(raw instanceof Map)) {
            throw new IllegalArgumentException();
        }

        Map<String, Object> params = (Map<String, Object>) raw;
        final HTTPDataSource httpDataSource = (HTTPDataSource) dataSource;

        final String url = createUrl();
        final RestTemplate restTemplate = httpDataSource.getRestTemplate();
        final HttpMethod method = HttpMethod.valueOf(MapUtils.getString(getParams(), "method"));

        final ResponseEntity<String> result;
        final ResponseExtractor<ResponseEntity<String>> responseExtractor = restTemplate.responseEntityExtractor(String.class);
        switch (method) {
            case GET:
            case HEAD:
                result = restTemplate.execute(url, method, null, responseExtractor, params);
                break;
            case POST:
            case PUT:
            case PATCH:
                final HttpEntity httpEntity = CollectionUtils.isEmpty(params) ? null : new HttpEntity(params);
                final RequestCallback requestCallback = restTemplate.httpEntityCallback(httpEntity, String.class);
                result = restTemplate.execute(url, method, requestCallback, responseExtractor);
                break;
            default:
                // bad method
                throw new IllegalArgumentException();
        }
        if (result == null || !HttpStatus.OK.equals(result.getStatusCode())) {
            // bad request
            return null;
        }

        final HttpMessageConverters httpMessageConverters = createDefaultHttpMessageConverters();

        final String body = result.getBody();
        try {
            final JsonNode jsonNode = objectMapper.readTree(body);
            if (jsonNode.isArray()) {
                return objectMapper.readValue(jsonNode.traverse(), List.class);
            } else {
                return objectMapper.readValue(jsonNode.traverse(), Map.class);
            }
        } catch (Exception e) {
            log.error("failed to parse value from body {}", body);
            return null;
        }
    }

    private HttpMessageConverters createDefaultHttpMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new ResourceRegionHttpMessageConverter());
        try {
            messageConverters.add(new SourceHttpMessageConverter<>());
        } catch (Throwable ex) {
            // Ignore when no TransformerFactory implementation is available...
        }
        messageConverters.add(new AllEncompassingFormHttpMessageConverter());

        if (jackson2XmlPresent) {
            Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.xml();
            messageConverters.add(new MappingJackson2XmlHttpMessageConverter(builder.build()));
        } else if (jaxb2Present) {
            messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
        }

        Jackson2ObjectMapperBuilder builder = Jackson2ObjectMapperBuilder.json();
        messageConverters.add(new MappingJackson2HttpMessageConverter(builder.build()));

        return new HttpMessageConverters(messageConverters);
    }

    private String createUrl() {
        final DataSource dataSource = getDataSource();
        final Properties properties = HTTPUtils.propertiesResolved(dataSource.getProperties().toJdkProperties());

        final URI mainUrl = URI.create(properties.getProperty("url"));

        final Map<String, Object> params = getParams();
        final String path = MapUtils.getString(params, "contextUrl");

        if (StringUtils.isBlank(path)) {
            return mainUrl.toString();
        }

        return mainUrl.resolve(path).toString();
    }

    static {
        objectMapper = new ObjectMapper();

        ClassLoader classLoader = WebMvcConfigurationSupport.class.getClassLoader();
        romePresent = ClassUtils.isPresent("com.rometools.rome.feed.WireFeed", classLoader);
        jaxb2Present = ClassUtils.isPresent("javax.xml.bind.Binder", classLoader);
        jackson2Present = ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", classLoader) &&
            ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", classLoader);
        jackson2XmlPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.xml.XmlMapper", classLoader);
        jackson2SmilePresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.smile.SmileFactory", classLoader);
        jackson2CborPresent = ClassUtils.isPresent("com.fasterxml.jackson.dataformat.cbor.CBORFactory", classLoader);
        gsonPresent = ClassUtils.isPresent("com.google.gson.Gson", classLoader);
        jsonbPresent = ClassUtils.isPresent("javax.json.bind.Jsonb", classLoader);
    }

}
