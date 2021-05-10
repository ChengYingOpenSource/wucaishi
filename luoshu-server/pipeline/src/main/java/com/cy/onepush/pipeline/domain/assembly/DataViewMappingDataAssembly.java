package com.cy.onepush.pipeline.domain.assembly;

import com.cy.onepush.pipeline.domain.DataViewMappingData;
import com.cy.onepush.pipeline.interfaces.params.DataViewMappingParam;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper
public interface DataViewMappingDataAssembly {

    DataViewMappingDataAssembly ASSEMBLY = Mappers.getMapper(DataViewMappingDataAssembly.class);

    default DataViewMappingData dataFromParam(DataViewMappingParam param) {
        final String name = StringUtils.isBlank(param.getDataViewName()) ? param.getDataViewCode() : param.getDataViewName();
        return new DataViewMappingData(param.getAlias(), param.getDataViewCode(), param.getDataViewType(),
            name, param.getDataViewParams(), param.getDataSourceCode(),
            param.getRequestDataStructure(), param.getResponseDataStructure());
    }

    List<DataViewMappingData> dataFromParams(Collection<DataViewMappingParam> collection);

}
