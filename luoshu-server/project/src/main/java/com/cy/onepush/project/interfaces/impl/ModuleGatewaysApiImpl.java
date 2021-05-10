package com.cy.onepush.project.interfaces.impl;

import com.cy.onepush.common.framework.infrastructure.repository.page.PageInfo;
import com.cy.onepush.common.framework.infrastructure.web.PaginationResult;
import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.common.utils.URLAppender;
import com.cy.onepush.gateway.application.GatewayApplicationService;
import com.cy.onepush.gateway.domain.Gateway;
import com.cy.onepush.pipeline.application.PipelineApplicationService;
import com.cy.onepush.pipeline.interfaces.params.PipelineCreateGatewayParam;
import com.cy.onepush.project.application.ProjectApplicationService;
import com.cy.onepush.project.domain.Project;
import com.cy.onepush.project.interfaces.ModuleGatewaysApi;
import com.cy.onepush.project.interfaces.assembly.ModuleGatewayVOAssembly;
import com.cy.onepush.project.interfaces.params.SearchModuleGatewayParams;
import com.cy.onepush.project.interfaces.vo.ModuleGatewayVO;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class ModuleGatewaysApiImpl implements ModuleGatewaysApi {

    private final GatewayApplicationService gatewayApplicationService;
    private final ProjectApplicationService projectApplicationService;
    private final PipelineApplicationService pipelineApplicationService;

    @Override
    public PaginationResult<ModuleGatewayVO> list(String moduleCode, SearchModuleGatewayParams params) {
        final Project project = projectApplicationService.getByModuleCode(moduleCode);
        final PageInfo<Gateway> pageInfo = gatewayApplicationService.pagination(moduleCode, params.getName(), params.getStatus(),
            params.getPageNum(), params.getPageSize());

        return PaginationResult.<ModuleGatewayVO>paginationBuilder()
            .list(ModuleGatewayVOAssembly.ASSEMBLY.vosFromDomains(project.getId().getId(), moduleCode, pageInfo.getList()))
            .pagination(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal())
            .build();
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public Result<String> createGateway(String moduleCode, PipelineCreateGatewayParam param) {
        // TODO move add gateway logic into pipeline application service
        final Project project = projectApplicationService.getByModuleCode(moduleCode);
        final String dataApiContextUrl = URLAppender.getInstance().append("data").append(project.getId().getId()).append(moduleCode).build();
        final Gateway gateway = pipelineApplicationService.pipelineCreate(dataApiContextUrl, param);

        // bind relation
        projectApplicationService.addGateway(project.getId().getId(), moduleCode, gateway.getId().getId(), param.getVersion());

        return Result.<String>builder().success(gateway.getId().getId()).build();
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public Result<Boolean> updateGateway(String moduleCode, String gatewayCode, String version, PipelineCreateGatewayParam param) {
        final Project project = projectApplicationService.getByModuleCode(moduleCode);
        final String dataApiContextUrl = URLAppender.getInstance().append("data").append(project.getId().getId()).append(moduleCode).build();
        pipelineApplicationService.pipelineUpdate(dataApiContextUrl, gatewayCode, param, version);
        return Result.<Boolean>builder().success(Boolean.TRUE).build();
    }

}
