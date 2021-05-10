package com.cy.onepush.pipeline.domain.assembly;

import com.cy.onepush.pipeline.domain.GatewayData;
import com.cy.onepush.pipeline.interfaces.params.GatewayParam;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GatewayDataAssembly {

    GatewayDataAssembly ASSEMBLY = Mappers.getMapper(GatewayDataAssembly.class);

    default GatewayData dataFromParam(GatewayParam param, String version) {
        final String name = StringUtils.isBlank(param.getGatewayName()) ? param.getGatewayCode() : param.getGatewayName();
        return new GatewayData(param.getGatewayCode(), name,
            param.getGatewayMethod(), param.getGatewayUri(), param.getGatewayDescription(), version);
    }

}
