package com.cy.onepush.pipeline.interfaces.assemly;

import com.cy.onepush.pipeline.domain.DataPackagerData;
import com.cy.onepush.pipeline.domain.DataViewMappingData;
import com.cy.onepush.pipeline.domain.GatewayData;
import com.cy.onepush.pipeline.domain.Pipeline;
import com.cy.onepush.pipeline.interfaces.params.DataPackagerParam;
import com.cy.onepush.pipeline.interfaces.params.DataViewMappingParam;
import com.cy.onepush.pipeline.interfaces.params.GatewayParam;
import com.cy.onepush.pipeline.interfaces.vo.PipelineVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface PipelineVOAssembly {

    PipelineVOAssembly ASSEMBLY = Mappers.getMapper(PipelineVOAssembly.class);

    @Mapping(target = "version", source = "gatewayData.version")
    @Mapping(target = "gatewayParam", source = "gatewayData")
    @Mapping(target = "dataPackagerParam", source = "dataPackagerData")
    @Mapping(target = "dataViewMappingParams", source = "dataViewMappingDataMap")
    PipelineVO voFromDomain(Pipeline pipeline);

    GatewayParam gatewayParamFromData(GatewayData data);

    DataPackagerParam dataPackagerParamFromData(DataPackagerData data);

    DataViewMappingParam dataViewMappingParamFromData(DataViewMappingData data);

    default List<DataViewMappingParam> dataPackagerParamsFromData(Map<String, DataViewMappingData> map) {
        return map.values().parallelStream()
            .map(PipelineVOAssembly.ASSEMBLY::dataViewMappingParamFromData)
            .collect(Collectors.toList());
    }

}
