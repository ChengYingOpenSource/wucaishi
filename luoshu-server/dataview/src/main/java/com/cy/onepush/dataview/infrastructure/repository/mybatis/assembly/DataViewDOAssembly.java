package com.cy.onepush.dataview.infrastructure.repository.mybatis.assembly;

import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.dataview.infrastructure.repository.mybatis.bean.DataViewDO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface DataViewDOAssembly {

    DataViewDOAssembly ASSEMBLY = Mappers.getMapper(DataViewDOAssembly.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", source = "dataView.id.id")
    @Mapping(target = "name", source = "dataView.name")
    @Mapping(target = "params", expression = "java( DataViewDOAssembly.ASSEMBLY.paramsFromMap(objectMapper, dataView.getParams()) )")
    @Mapping(target = "datasourceCode", source = "dataView.dataSource.id.id")
    @Mapping(target = "reqDatastructureCode", source = "dataView.requestDataStructure.id.id")
    @Mapping(target = "respDatastructureCode", source = "dataView.responseDataStructure.id.id")
    @Mapping(target = "version", source = "dataView.dataViewVersion.id")
    @Mapping(target = "gmtCreated", source = "date")
    @Mapping(target = "gmtModified", source = "date")
    @Mapping(target = "gmtCreator", expression = "java( -1L )")
    @Mapping(target = "gmtModifier", expression = "java( -1L )")
    DataViewDO doFromDomain(ObjectMapper objectMapper, DataView dataView, Date date);

    default List<DataViewDO> dosFromDomains(ObjectMapper objectMapper, Collection<DataView> collection, Date date) {
        return collection.stream()
            .map(item -> DataViewDOAssembly.ASSEMBLY.doFromDomain(objectMapper, item, date))
            .collect(Collectors.toList());
    }

    default String paramsFromMap(ObjectMapper objectMapper, Map<String, Object> params) {
        try {
            return objectMapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            return StringUtils.EMPTY;
        }
    }

}
