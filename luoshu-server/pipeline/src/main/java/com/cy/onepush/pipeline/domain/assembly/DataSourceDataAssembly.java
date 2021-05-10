package com.cy.onepush.pipeline.domain.assembly;

import com.cy.onepush.pipeline.interfaces.params.DataSourceParam;
import com.cy.onepush.pipeline.domain.DataSourceData;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DataSourceDataAssembly {

    DataSourceDataAssembly ASSEMBLY = Mappers.getMapper(DataSourceDataAssembly.class);

    default DataSourceData dataFromParam(DataSourceParam param) {
        return new DataSourceData(param.getDataSourceCode(), param.getDataSourceName(),
            param.getDataSourceType(), param.getDataSourceProperties());
    }

}
