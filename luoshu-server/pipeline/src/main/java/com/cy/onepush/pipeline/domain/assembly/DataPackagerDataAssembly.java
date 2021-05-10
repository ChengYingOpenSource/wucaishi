package com.cy.onepush.pipeline.domain.assembly;

import com.cy.onepush.pipeline.interfaces.params.DataPackagerParam;
import com.cy.onepush.pipeline.domain.DataPackagerData;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DataPackagerDataAssembly {

    DataPackagerDataAssembly ASSEMBLY = Mappers.getMapper(DataPackagerDataAssembly.class);

    default DataPackagerData dataFromParam(DataPackagerParam param) {
        final String name = StringUtils.isBlank(param.getDataPackagerName()) ? param.getDataPackagerCode() : param.getDataPackagerName();
        return new DataPackagerData(param.getDataPackagerCode(), name,
            param.getScriptType(), param.getScriptContent(),
            param.getRequestDataStructure(), param.getResponseDataStructure());
    }

}
