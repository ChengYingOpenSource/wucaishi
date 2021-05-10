package com.cy.onepush.dataview.infrastructure.repository;

import com.cy.onepush.datastructure.domain.DataStructure;
import com.cy.onepush.datastructure.domain.DataStructureRepository;
import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.dataview.domain.DataViewIdWithVersion;
import com.cy.onepush.dataview.domain.DataViewRepository;
import com.cy.onepush.dataview.infrastructure.repository.mybatis.assembly.CodeAndVersionsParamAssembly;
import com.cy.onepush.dataview.infrastructure.repository.mybatis.assembly.DataViewDOAssembly;
import com.cy.onepush.dataview.infrastructure.repository.mybatis.bean.DataViewDO;
import com.cy.onepush.dataview.infrastructure.repository.mybatis.mapper.DataViewMapper;
import com.cy.onepush.dataview.infrastructure.repository.mybatis.params.CodeAndVersionsParam;
import com.cy.onepush.dataview.interfaces.params.DebugDataViewParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component("rdsDataViewRepository")
@RequiredArgsConstructor
public class RdsDataViewRepository implements DataViewRepository {

    private final DataViewMapper dataViewMapper;

    private final DataStructureRepository dataStructureRepository;

    private final ObjectMapper objectMapper;

    @Override
    public DataView get(DataViewIdWithVersion dataViewIdWithVersion) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataView dataViewForDebug(DebugDataViewParams params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DataView> batchGet(Collection<DataViewIdWithVersion> dataViewIdWithVersions) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<DataView> all() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(DataView dataView) {
        // assembly to do
        final DataViewDO dataViewDO = DataViewDOAssembly.ASSEMBLY.doFromDomain(objectMapper, dataView, new Date());

        // insert into db
        dataViewMapper.insert(dataViewDO);
    }

    @Override
    public void batchAdd(Collection<DataView> collection) {
        // insert into db
        final List<DataViewDO> dataViewDOS = DataViewDOAssembly.ASSEMBLY.dosFromDomains(objectMapper, collection, new Date());
        dataViewMapper.batchInsert(dataViewDOS);

        // insert req and resp datastructures into db
        final Map<String, Collection<DataStructure>> dataViewCodes = collection.stream()
            .map(item -> {
                final List<DataStructure> requestDataStructure = item.getRequestDataStructure().getDataStructures();
                final List<DataStructure> responseDataStructure = item.getResponseDataStructure().getDataStructures();

                return Lists.newArrayList(
                    new AbstractMap.SimpleEntry<>(item.getRequestDataStructure().getId().getId() + "_" + item.getDataViewVersion().getId(), requestDataStructure),
                    new AbstractMap.SimpleEntry<>(item.getResponseDataStructure().getId().getId() + "_" + item.getDataViewVersion().getId(), responseDataStructure)
                );
            })
            .flatMap(Collection::stream)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
        dataStructureRepository.batchAdd(dataViewCodes);
    }

    @Override
    public void update(DataView dataView) {
        // assembly to do
        final DataViewDO dataViewDO = DataViewDOAssembly.ASSEMBLY.doFromDomain(objectMapper, dataView, new Date());

        // insert into db
        dataViewMapper.updateByPrimaryKey(dataViewDO);
    }

    @Override
    public void batchInsertOrUpdate(Collection<DataView> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return;
        }
        // assembly to do
        final List<DataViewDO> dataViewDOS = DataViewDOAssembly.ASSEMBLY.dosFromDomains(objectMapper, collection, new Date());

        // insert or update into db
        dataViewMapper.batchInsertOrUpdate(dataViewDOS);

        // insert or update req and resp datastructures into db
        final Map<String, Collection<DataStructure>> dataViewCodes = collection.stream()
            .map(item -> {
                final List<DataStructure> requestDataStructure = item.getRequestDataStructure().getDataStructures();
                final List<DataStructure> responseDataStructure = item.getResponseDataStructure().getDataStructures();

                return Lists.newArrayList(
                    new AbstractMap.SimpleEntry<>(item.getRequestDataStructure().getId().getId() + "_" + item.getDataViewVersion().getId(), requestDataStructure),
                    new AbstractMap.SimpleEntry<>(item.getResponseDataStructure().getId().getId() + "_" + item.getDataViewVersion().getId(), responseDataStructure)
                );
            })
            .flatMap(Collection::stream)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
        dataStructureRepository.batchInsertOrUpdate(dataViewCodes);
    }

    @Override
    public void delete(DataView dataView) {
        // delete data view
        dataViewMapper.deleteByCodeAndVersion(dataView.getId().getId(), dataView.getDataViewVersion().getId());

        // delete data structures
        dataStructureRepository.batchDeleteByTargetCode(Lists.newArrayList(
            dataView.getRequestDataStructure().getId().getId() + "_" + dataView.getDataViewVersion().getId(),
            dataView.getResponseDataStructure().getId().getId() + "_" + dataView.getDataViewVersion().getId()
        ));
    }

    @Override
    public void batchDeleteByIds(Collection<DataViewIdWithVersion> dataViewIds) {
        if (CollectionUtils.isEmpty(dataViewIds)) {
            return;
        }

        final List<CodeAndVersionsParam> params = CodeAndVersionsParamAssembly.ASSEMBLY.paramsFromDomains(dataViewIds);
        dataViewMapper.batchDeleteByCodeAndVersions(params);


    }

}
