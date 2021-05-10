package com.cy.onepush.dataview.infrastructure.plugin.assembly;

import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.datasource.domain.DataSourceRepository;
import com.cy.onepush.datastructure.domain.DataStructureRepository;
import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.dataview.infrastructure.repository.mybatis.bean.DataViewDO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface DataViewAssembly {

    TypeReference<Map<String, Object>> PARAM_TYPE_REFERENCE = new TypeReference<Map<String, Object>>() {
    };

    DataViewAssembly ASSEMBLY = Mappers.getMapper(DataViewAssembly.class);

    default DataView domainFromDo(ObjectMapper objectMapper,
                                  DataSourceRepository dataSourceRepository,
                                  DataStructureRepository dataStructureRepository,
                                  DataViewDO dataViewDO) {
        final DataView dataView = new DataView(DataViewId.of(dataViewDO.getCode()), dataViewDO.getName(), Version.of(dataViewDO.getVersion()));

        try {
            dataView.setParams(objectMapper.readValue(dataViewDO.getParams(), PARAM_TYPE_REFERENCE));
        } catch (JsonProcessingException e) {
            dataView.setParams(new HashMap<>());
        }

        dataView.bindDataSource(dataSourceRepository.getById(DataSourceId.of(dataViewDO.getDatasourceCode())));
        dataView.addAllRequestDataStructure(
            dataStructureRepository.getByTargetCode(dataView.getRequestDataStructure().getId().getId() + "_" + dataViewDO.getVersion())
                .orElse(Lists.newArrayListWithCapacity(0)));
        dataView.addAllResponseDataStructure(
            dataStructureRepository.getByTargetCode(dataView.getResponseDataStructure().getId().getId() + "_" + dataViewDO.getVersion())
                .orElse(Lists.newArrayListWithCapacity(0)));

        return dataView;
    }

    default List<DataView> domainsFromDos(ObjectMapper objectMapper,
                                          DataSourceRepository dataSourceRepository,
                                          DataStructureRepository dataStructureRepository,
                                          Collection<DataViewDO> collection) {
        return collection.stream()
            .map(item -> DataViewAssembly.ASSEMBLY.domainFromDo(objectMapper, dataSourceRepository, dataStructureRepository, item))
            .collect(Collectors.toList());
    }

}
