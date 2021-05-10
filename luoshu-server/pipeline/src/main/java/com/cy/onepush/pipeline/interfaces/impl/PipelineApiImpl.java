package com.cy.onepush.pipeline.interfaces.impl;

import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.pipeline.application.PipelineApplicationService;
import com.cy.onepush.pipeline.domain.Pipeline;
import com.cy.onepush.pipeline.interfaces.PipelineApi;
import com.cy.onepush.pipeline.interfaces.assemly.PipelineVOAssembly;
import com.cy.onepush.pipeline.interfaces.vo.PipelineVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PipelineApiImpl implements PipelineApi {

    private final PipelineApplicationService pipelineApplicationService;

    @Override
    public Result<PipelineVO> get(String gatewayCode, String version) {
        final Pipeline pipeline = pipelineApplicationService.getByGatewayCodeAndVersion(gatewayCode, version);
        final PipelineVO pipelineVO = PipelineVOAssembly.ASSEMBLY.voFromDomain(pipeline);

        return Result.<PipelineVO>builder()
            .data(pipelineVO)
            .build();
    }

}
