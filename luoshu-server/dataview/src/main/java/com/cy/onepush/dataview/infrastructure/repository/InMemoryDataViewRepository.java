package com.cy.onepush.dataview.infrastructure.repository;

import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.datasource.domain.DataSourceRepository;
import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.dataview.domain.DataViewIdWithVersion;
import com.cy.onepush.dataview.domain.DataViewRepository;
import com.cy.onepush.dataview.infrastructure.holder.DataViewHolder;
import com.cy.onepush.dataview.interfaces.params.DebugDataViewParams;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Primary
@Component
public class InMemoryDataViewRepository implements DataViewRepository {

    private final DataViewRepository rdsDataViewRepository;
    private final DataSourceRepository dataSourceRepository;

    public InMemoryDataViewRepository(@Qualifier("rdsDataViewRepository") DataViewRepository rdsDataViewRepository,
                                      DataSourceRepository dataSourceRepository) {
        this.rdsDataViewRepository = rdsDataViewRepository;
        this.dataSourceRepository = dataSourceRepository;
    }

    @Override
    public DataView get(DataViewIdWithVersion dataViewIdWithVersion) {
        return DataViewHolder.INSTANCE.getById(dataViewIdWithVersion);
    }

    @Override
    public DataView dataViewForDebug(DebugDataViewParams params) {
        // uuid
        final String uid = params.getDataViewCode();

        // get datasource
        final DataSource dataSource = dataSourceRepository.getById(DataSourceId.of(params.getDataSourceCode()));

        // get the data view
        final DataView dataView = new DataView(DataViewId.of(uid), uid);
        dataView.setParams(new HashMap<>(params.getDataViewParams()));
        dataView.bindDataSource(dataSource);
        dataView.bindRequestDataStructure(params.getRequestDataStructure());
        dataView.bindResponseDataStructure(params.getResponseDataStructure());

        return dataView;
    }

    @Override
    public List<DataView> batchGet(Collection<DataViewIdWithVersion> dataViewIdWithVersions) {
        return dataViewIdWithVersions.parallelStream()
            .map(item -> DataViewHolder.INSTANCE.getById(DataViewIdWithVersion.of(item.getDataViewId(), item.getVersion())))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    @Override
    public Collection<DataView> all() {
        return ImmutableList.copyOf(DataViewHolder.INSTANCE.all());
    }

    @Override
    public void add(DataView dataView) {
        rdsDataViewRepository.add(dataView);
    }

    @Override
    public void batchAdd(Collection<DataView> collection) {
        rdsDataViewRepository.batchAdd(collection);
    }

    @Override
    public void update(DataView dataView) {
        rdsDataViewRepository.update(dataView);
    }

    @Override
    public void batchInsertOrUpdate(Collection<DataView> collection) {
        rdsDataViewRepository.batchInsertOrUpdate(collection);
    }

    @Override
    public void delete(DataView dataView) {
        rdsDataViewRepository.delete(dataView);
    }

    @Override
    public void batchDeleteByIds(Collection<DataViewIdWithVersion> dataViewIdWithVersions) {
        rdsDataViewRepository.batchDeleteByIds(dataViewIdWithVersions);
    }

}
