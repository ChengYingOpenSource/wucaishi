package com.cy.onepush.pipeline.interfaces.params;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 步骤性创建网关参数
 *
 * @author WhatAKitty
 * @date 2020-12-17
 * @since 0.1.0
 */
@Data
public class PipelineUpdateGatewayParam {

    @Valid
    @NotNull(message = "gateway param should not be null")
    private GatewayParam gatewayParam;
    @Valid
    @NotNull(message = "data packager param should not be null")
    private DataPackagerParam dataPackagerParam;
    @Valid
    @NotEmpty(message = "data view mapping params should not be empty")
    private List<@NotNull(message = "data view mapping item should not be null") DataViewMappingParam> dataViewMappingParams;

}
