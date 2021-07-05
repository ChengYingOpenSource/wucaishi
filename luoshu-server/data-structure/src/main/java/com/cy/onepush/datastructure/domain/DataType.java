package com.cy.onepush.datastructure.domain;

import com.cy.onepush.common.exception.Asserts;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public enum DataType {

    NONE(0, false, (dataStructure, raw) -> null),
    INT(1, (dataStructure, raw) -> {
        if (raw == null && dataStructure.isRequired()) {
            throw new IllegalArgumentException();
        }
        if (raw == null) {
            return null;
        }

        if (raw instanceof Number) {
            return ((Number) raw).intValue();
        }

        if (raw instanceof String) {
            try {
                return Integer.parseInt((java.lang.String) raw);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException();
            }
        }

        throw new IllegalArgumentException();
    }),
    LONG(2, (dataStructure, raw) -> {
        if (raw == null && dataStructure.isRequired()) {
            throw new IllegalArgumentException();
        }
        if (raw == null) {
            return null;
        }

        if (raw instanceof Number) {
            return ((Number) raw).longValue();
        }

        if (raw instanceof String) {
            try {
                return Long.parseLong((java.lang.String) raw);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException();
            }
        }

        throw new IllegalArgumentException();
    }),
    STRING(3, (dataStructure, raw) -> {
        if (raw == null && dataStructure.isRequired()) {
            throw new IllegalArgumentException();
        }
        if (raw == null) {
            return null;
        }

        if (raw instanceof String) {
            if (StringUtils.isBlank((CharSequence) raw) && dataStructure.isRequired()) {
                throw new IllegalArgumentException();
            }
            return raw;
        }

        return raw.toString();
    }),
    BOOL(4, (dataStructure, raw) -> {
        if (raw == null && dataStructure.isRequired()) {
            throw new IllegalArgumentException();
        }
        if (raw == null) {
            return null;
        }

        if (raw instanceof Boolean) {
            return raw;
        } else if (raw instanceof String) {
            if (StringUtils.isBlank((String) raw) && dataStructure.isRequired()) {
                throw new IllegalArgumentException();
            }
            return Boolean.parseBoolean((String) raw);
        }

        throw new IllegalArgumentException();
    }),
    DECIMAL(5, (dataStructure, raw) -> {
        if (raw == null && dataStructure.isRequired()) {
            throw new IllegalArgumentException();
        }

        if (raw instanceof Number || raw instanceof String) {
            final String rawStr = String.valueOf(raw);
            if (StringUtils.isBlank(rawStr) && dataStructure.isRequired()) {
                throw new IllegalArgumentException();
            }

            try {
                return new BigDecimal(rawStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException();
            }
        }

        throw new IllegalArgumentException();
    }),
    DATE(6, (dataStructure, raw) -> {
        if (raw == null && dataStructure.isRequired()) {
            throw new IllegalArgumentException();
        }
        if (raw == null) {
            return null;
        }

        if (raw instanceof Date) {
            return raw;
        }

        if (raw instanceof String) {
            try {
                return DateUtils.parseDate((String) raw,
                    "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "dd/MM/yyyy HH:mm:ss",
                    "yyyy-MM-dd", "yyyy/MM/dd", "dd/MM/yyyy",
                    "yyyy-MM-dd HH:mm:ss.SSS", "yyyy/MM/dd HH:mm:ss.SSS", "dd/MM/yyyy HH:mm:ss.SSS"
                );
            } catch (ParseException e) {
                throw new IllegalArgumentException();
            }
        }

        throw new IllegalArgumentException();
    }),
    @SuppressWarnings("unchecked")
    COLLECTION(98, (dataStructure, raw) -> {
        if (raw == null && dataStructure.isRequired()) {
            throw new IllegalArgumentException();
        }
        if (raw == null) {
            return null;
        }

        if (!(raw instanceof Collection)) {
            throw new IllegalArgumentException();
        }

        if (((Collection<Map<String, Object>>) raw).isEmpty() && dataStructure.isRequired()) {
            throw new IllegalArgumentException();
        }

        final List<Map<String, Object>> result = ((Collection<Map<String, Object>>) raw).stream()
            .map(dataItem -> dataStructure.getDataStructures().stream()
                .map(dataStructureItem -> {
                    final DataType dataType = dataStructureItem.getDataType();
                    final boolean isObject = dataType.isObject();
                    final String field = dataStructureItem.getField();

                    final Object itemResult = dataType.resolve(dataStructureItem, dataItem.get(field));
                    final Object itemResolved = isObject ? ((Map<String, Object>) itemResult).get(field) : itemResult;
                    return new AbstractMap.SimpleEntry<>(field, itemResolved);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a)))
            .collect(Collectors.toList());

        return ImmutableMap.of(
            dataStructure.getField(), result
        );
    }),
    @SuppressWarnings("unchecked")
    ITEM(97, (dataStructure, raw) -> {
        if (raw == null && dataStructure.isRequired()) {
            throw new IllegalArgumentException();
        }
        if (raw == null) {
            return null;
        }

        if (!(raw instanceof Map)) {
            throw new IllegalArgumentException();
        }

        final Map<String, Object> result = new HashMap<>();
        dataStructure.getDataStructures().parallelStream()
            .forEach(dataStructureItem -> {
                final DataType dataType = dataStructureItem.getDataType();
                final boolean isObject = dataType.isObject();
                final String field = dataStructureItem.getField();

                final Object itemResult = dataType.resolve(dataStructureItem, ((Map<?, ?>) raw).get(field));
                final Object itemResolved = null == itemResult ? null : (isObject ? ((Map<String, Object>) itemResult).get(field) : itemResult);
                result.put(field, itemResolved);
            });

        return ImmutableMap.of(
            dataStructure.getField(), result
        );
    }),
    ANY(99, false, (dataStructure, raw) -> {
        if (raw == null && dataStructure.isRequired()) {
            throw new IllegalArgumentException();
        }
        if (raw == null) {
            return null;
        }

        if (raw instanceof Collection) {
            return DataType.COLLECTION.resolve(dataStructure, raw);
        }

        if (raw instanceof Map) {
            return DataType.ITEM.resolve(dataStructure, raw);
        }

        if (raw instanceof Integer) {
            return DataType.INT.resolve(dataStructure, raw);
        }

        if (raw instanceof BigDecimal) {
            return DataType.DECIMAL.resolve(dataStructure, raw);
        }

        if (raw instanceof Date) {
            return DataType.DATE.resolve(dataStructure, raw);
        }

        if (raw instanceof String) {
            try {
                return DataType.INT.resolve(dataStructure, raw);
            } catch (Exception e) {
                // ignore
            }

            try {
                return DataType.DECIMAL.resolve(dataStructure, raw);
            } catch (Exception e) {
                // ignore
            }

            try {
                return DataType.DATE.resolve(dataStructure, raw);
            } catch (Exception e) {
                // ignore
            }

            return DataType.STRING.resolve(dataStructure, raw);
        }

        throw new IllegalArgumentException();
    });

    public static DataType of(String raw) {
        final String dataTypeStr = StringUtils.trim(raw);

        final DataType dataType = Arrays.stream(DataType.values())
            .filter(item -> item.name().equalsIgnoreCase(dataTypeStr))
            .findFirst()
            .orElse(null);
        Asserts.assertNotNull(dataType, dataTypeStr);

        return dataType;
    }

    @Getter
    private final int sort;
    private final Resolver resolver;
    @Getter
    private final boolean visible;

    DataType(int sort, Resolver resolver) {
        this(sort, Boolean.TRUE, resolver);
    }

    DataType(int sort, boolean visible, Resolver resolver) {
        this.sort = sort;
        this.resolver = resolver;
        this.visible = visible;
    }

    public Object resolve(DataStructure dataStructure, Object val) throws IllegalArgumentException {
        return resolver.resolve(dataStructure, val);
    }

    private boolean isObject() {
        return COLLECTION.equals(this) || ITEM.equals(this);
    }

    @FunctionalInterface
    interface Resolver {

        Object resolve(DataStructure dataStructure, Object val);

    }

}
