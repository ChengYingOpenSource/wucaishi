package com.cy.onepush.pipeline.infrastructure.repository;

import com.cy.onepush.common.publishlanguage.pipeline.PipelineId;
import com.cy.onepush.gateway.domain.GatewayIdWithVersion;
import com.cy.onepush.pipeline.domain.Pipeline;
import com.cy.onepush.pipeline.domain.PipelineRepository;
import com.cy.onepush.pipeline.infrastructure.repository.mybatis.assembly.PipelineDOAssembly;
import com.cy.onepush.pipeline.infrastructure.repository.mybatis.bean.PipelineDO;
import com.cy.onepush.pipeline.infrastructure.repository.mybatis.mapper.PipelineMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RdsPipelineRepository implements PipelineRepository {

    private final PipelineMapper pipelineMapper;

    @Override
    public Optional<Pipeline> get(PipelineId pipelineId) {
        final PipelineDO pipelineDO = pipelineMapper.selectByCode(pipelineId.getId());
        return Optional.ofNullable(PipelineDOAssembly.ASSEMBLY.domainFromDo(pipelineDO));
    }

    @Override
    public Optional<Pipeline> getByGatewayIdWithVersion(GatewayIdWithVersion gatewayIdWithVersion) {
        final PipelineDO pipelineDO = pipelineMapper.selectByGatewayCodeAndVersion(gatewayIdWithVersion.getGatewayId().getId(),
            gatewayIdWithVersion.getVersion().getId());
        return Optional.ofNullable(PipelineDOAssembly.ASSEMBLY.domainFromDo(pipelineDO));
    }

    @Override
    public void add(Pipeline pipeline) {
        final PipelineDO pipelineDO = PipelineDOAssembly.ASSEMBLY.doFromDomain(pipeline, new Date());

        pipelineMapper.insert(pipelineDO);
    }

    @Override
    public void update(Pipeline pipeline) {
        final PipelineDO pipelineDO = PipelineDOAssembly.ASSEMBLY.doFromDomain(pipeline, new Date());

        pipelineMapper.updateByCodeAndVersionWithBLOBs(pipelineDO);
    }
}
