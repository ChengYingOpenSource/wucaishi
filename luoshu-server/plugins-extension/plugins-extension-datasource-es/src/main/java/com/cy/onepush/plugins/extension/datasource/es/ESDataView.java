package com.cy.onepush.plugins.extension.datasource.es;

import com.cy.onepush.common.exception.Asserts;
import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.common.utils.NamedParameterUtils;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.dataview.domain.ParamsDataView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class ESDataView extends ParamsDataView {

    private static final ObjectMapper objectMapper;

    public ESDataView(DataViewId id, String name) {
        super(id, name);
    }

    public ESDataView(DataViewId id, String name, Version version) {
        super(id, name, version);
    }

    @Override
    protected boolean validateParams() {
        final Map<String, Object> params = getParams();
        Asserts.assertTrue(params.containsKey("contextUrl"), "the context is empty");
        Asserts.assertTrue(params.containsKey("method"), "the method is empty");
        Asserts.assertTrue(params.containsKey("template"), "the template is empty");
        return true;
    }

    @Override
    protected Object _execute(DataSource dataSource, Object raw) {
        assert dataSource instanceof ESDataSource;

        // params pre handle
        if (raw != null && !(raw instanceof Map)) {
            throw new IllegalArgumentException();
        }

        Map<String, Object> params = (Map<String, Object>) raw;
        final ESDataSource esDataSource = (ESDataSource) dataSource;

        final String url = createUrl();
        final RestTemplate restTemplate = esDataSource.getRestTemplate();
        final HttpMethod method = HttpMethod.valueOf(MapUtils.getString(getParams(), "method"));

        final ResponseEntity<String> result;
        final ResponseExtractor<ResponseEntity<String>> responseExtractor = restTemplate.responseEntityExtractor(String.class);
        switch (method) {
            case GET:
            case HEAD:
                result = restTemplate.execute(url, method, null, responseExtractor, params);
                break;
            case POST:
                // create request body by es template
                final String template = MapUtils.getString(params, "template");
                final String requestBody = NamedParameterUtils.replace(template, params);

                // request with body
                final HttpEntity httpEntity = new HttpEntity(requestBody);
                final RequestCallback requestCallback = restTemplate.httpEntityCallback(httpEntity, String.class);
                result = restTemplate.execute(url, method, requestCallback, responseExtractor);
                break;
            case PUT:
            case PATCH:
            default:
                // bad method
                throw new IllegalArgumentException();
        }
        if (result == null || !HttpStatus.OK.equals(result.getStatusCode())) {
            // bad request
            return null;
        }

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

    private String createUrl() {
        final DataSource dataSource = getDataSource();
        final Properties properties = ESUtils.propertiesResolved(dataSource.getProperties().toJdkProperties());

        final String mainUrl = properties.getProperty("url");

        final Map<String, Object> params = getParams();
        final String path = MapUtils.getString(params, "contextUrl");

        if (StringUtils.isBlank(path)) {
            return mainUrl;
        }

        return String.format("%s/%s", mainUrl, path);
    }

    static {
        objectMapper = new ObjectMapper();
    }

}
