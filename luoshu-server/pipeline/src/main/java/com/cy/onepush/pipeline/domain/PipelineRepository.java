package com.cy.onepush.pipeline.domain;

import com.cy.onepush.common.publishlanguage.pipeline.PipelineId;
import com.cy.onepush.gateway.domain.GatewayIdWithVersion;

import java.util.Optional;

/**
 * pipeline repository
 *
 * @author WhatAKitty
 * @date 2020-12-17
 * @since 0.1.0
 */
public interface PipelineRepository {

    Optional<Pipeline> get(PipelineId pipelineId);

    Optional<Pipeline> getByGatewayIdWithVersion(GatewayIdWithVersion gatewayIdWithVersion);

    void add(Pipeline pipeline);

    void update(Pipeline pipeline);

}
