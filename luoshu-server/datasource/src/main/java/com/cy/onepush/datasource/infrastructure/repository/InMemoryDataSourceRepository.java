package com.cy.onepush.datasource.infrastructure.repository;

import com.cy.onepush.common.exception.DuplicateResourceException;
import com.cy.onepush.common.exception.ResourceNotFoundException;
import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.datasource.domain.DataSourceProperties;
import com.cy.onepush.datasource.domain.DataSourceRepository;
import com.cy.onepush.datasource.infrastructure.holder.DataSourceHolder;
import com.cy.onepush.datasource.infrastructure.repository.mybatis.assembly.DataSourceDOAssembly;
import com.cy.onepush.datasource.infrastructure.repository.mybatis.bean.DataSourceDO;
import com.cy.onepush.datasource.infrastructure.repository.mybatis.mapper.DataSourceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryDataSourceRepository implements DataSourceRepository {

    private final DataSourceMapper dataSourceMapper;

    @Override
    public List<DataSource> allDataSources() {
        return new ArrayList<>(DataSourceHolder.INSTANCE.all());
    }

    @Override
    public DataSource getById(DataSourceId dataSourceId) {
        return DataSourceHolder.INSTANCE.getById(dataSourceId);
    }

    @Override
    public List<DataSource> batchGetByIds(Collection<DataSourceId> dataSourceIds) {
        return Collections.unmodifiableList(dataSourceIds.parallelStream()
            .map(dataSourceId -> DataSourceHolder.INSTANCE.getById(dataSourceId))
            .filter(Objects::nonNull)
            .collect(Collectors.toList()));
    }

    @Override
    public Set<String> allDataSourceTypes() {
        return DataSourceHolder.INSTANCE.allTypes();
    }

    @Override
    public void add(DataSource dataSource) {
        synchronized (dataSource.getId().getId().intern()) {
            final DataSourceDO old = dataSourceMapper.selectByCode(dataSource.getId().getId());
            if (old != null) {
                throw new DuplicateResourceException("the datasource %s already exists", dataSource.getId().getId());
            }

            final DataSourceDO dataSourceDO = DataSourceDOAssembly.ASSEMBLY.doFromDomain(dataSource, new Date());
            dataSourceMapper.insertOrUpdate(dataSourceDO);
        }
    }

    @Override
    public void update(DataSource dataSource) {
        synchronized (dataSource.getId().getId().intern()) {
            final DataSourceDO old = dataSourceMapper.selectByCode(dataSource.getId().getId());
            if (old == null) {
                throw new ResourceNotFoundException("the datasource %s not found", dataSource.getId().getId());
            }

            final DataSourceDO dataSourceDO = DataSourceDOAssembly.ASSEMBLY.doFromDomain(dataSource, new Date());
            dataSourceMapper.insertOrUpdate(dataSourceDO);
        }
    }

    @Override
    public void addOrUpdate(DataSource dataSource) {
        final DataSourceDO dataSourceDO = DataSourceDOAssembly.ASSEMBLY.doFromDomain(dataSource, new Date());
        dataSourceMapper.insertOrUpdate(dataSourceDO);
    }

    @Override
    public void batchAddOrUpdate(Collection<DataSource> dataSources) {
        // batch insert
        final List<DataSourceDO> dataSourceDOS = DataSourceDOAssembly.ASSEMBLY.dosFromDomains(dataSources, new Date());
        dataSourceMapper.batchInsertOrUpdate(dataSourceDOS);
    }

    @Override
    public DataSource createTemp(String dataSourceType, Map<String, String> dataSourceProperties) {
        final String uid = UUID.randomUUID().toString();

        return new DataSource(DataSourceId.of(uid), uid, dataSourceType, DataSourceProperties.of(dataSourceProperties));
    }

    @Override
    public void delete(DataSource dataSource) {
        dataSourceMapper.deleteByCode(dataSource.getId().getId());
    }


}
