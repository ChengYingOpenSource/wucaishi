package com.cy.onepush.pipeline.interfaces.vo;

import com.cy.onepush.pipeline.interfaces.params.DataPackagerParam;
import com.cy.onepush.pipeline.interfaces.params.DataViewMappingParam;
import com.cy.onepush.pipeline.interfaces.params.GatewayParam;
import lombok.Data;

import java.util.List;

@Data
public class PipelineVO {

    private String version;
    private GatewayParam gatewayParam;
    private DataPackagerParam dataPackagerParam;
    private List<DataViewMappingParam> dataViewMappingParams;

}
