package com.cy.onepush.datasource.infrastructure.holder;

import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.datasource.domain.DataSource;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DataSourceHolder {

    public static DataSourceHolder INSTANCE = new DataSourceHolder();

    private final Map<DataSourceId, DataSource> HOLDER = new ConcurrentHashMap<>(16);

    public void replaceDataSource(DataSource dataSource) {
        HOLDER.put(dataSource.getId(), dataSource);
    }

    public void removeDataSource(DataSource dataSource) {
        HOLDER.remove(dataSource.getId());
    }

    public Collection<DataSource> all() {
        return Collections.unmodifiableCollection(HOLDER.values());
    }

    public DataSource getById(DataSourceId dataSourceId) {
        return HOLDER.get(dataSourceId);
    }

    public Set<String> allTypes() {
        return Collections.unmodifiableSet(HOLDER.values().parallelStream().map(DataSource::getType).collect(Collectors.toSet()));
    }

    public boolean contains(DataSource dataSource) {
        return HOLDER.values().parallelStream().anyMatch(item -> item.getId().equals(dataSource.getId()));
    }

    public boolean containsType(String dataSourceType) {
        return allTypes().contains(dataSourceType);
    }

}
