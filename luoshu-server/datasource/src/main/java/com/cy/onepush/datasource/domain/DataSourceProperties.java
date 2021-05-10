package com.cy.onepush.datasource.domain;

import com.cy.onepush.common.framework.domain.ValueObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
@EqualsAndHashCode(callSuper = false)
public class DataSourceProperties extends ValueObject {

    public static DataSourceProperties of(List<DataSourceProperty> properties) {
        final Map<String, DataSourceProperty> propertyMap = properties.stream()
            .collect(Collectors.toMap(DataSourceProperty::getKey, item -> item));
        return new DataSourceProperties(new HashMap<>(propertyMap));
    }

    public static DataSourceProperties of(Map<String, String> properties) {
        final Map<String, DataSourceProperty> propertyMap = properties.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, item -> DataSourceProperty.of(item.getKey(), item.getValue())));
        return new DataSourceProperties(new HashMap<>(propertyMap));
    }

    private final Map<String, DataSourceProperty> properties;

    public DataSourceProperties(Map<String, DataSourceProperty> properties) {
        this.properties = properties;
    }

    public String getOrDefault(String key, String defaultValue) {
        final DataSourceProperty dataSourceProperty = properties.get(key);
        if (dataSourceProperty == null) {
            return defaultValue;
        }

        return dataSourceProperty.getValue();
    }

    public Integer getIntOrDefault(String key, Integer defaultValue) {
        final String str = getOrDefault(key, null);
        if (StringUtils.isBlank(str)) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            // bad format
            log.error("the number named {} valued {} parsed failed and downgrade to default value {}", key, str, defaultValue);
            return defaultValue;
        }
    }

    public boolean containsAndHaveValue(String key) {
        if (!properties.containsKey(key)) {
            return false;
        }

        final String value = getOrDefault(key, null);
        return StringUtils.isNotBlank(value);
    }

    public Properties toJdkProperties() {
        final Properties jdkProperties = new Properties();
        this.properties.forEach((k, v) -> jdkProperties.put(k, v.getValue()));
        return jdkProperties;
    }

    @Override
    public String toString() {
        final Map<String, String> snapshot = new HashMap<>(properties.size());
        for (Map.Entry<String, DataSourceProperty> entry : properties.entrySet()) {
            final DataSourceProperty dataSourceProperty = entry.getValue();
            snapshot.put(dataSourceProperty.getKey(), dataSourceProperty.getValue());
        }

        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(snapshot);
        } catch (JsonProcessingException e) {
            return StringUtils.EMPTY;
        }
    }

}
