package com.cy.onepush.project.interfaces.impl;

import com.cy.onepush.common.framework.infrastructure.repository.page.PageHelper;
import com.cy.onepush.common.framework.infrastructure.repository.page.PageInfo;
import com.cy.onepush.common.framework.infrastructure.web.PaginationResult;
import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.project.application.ProjectApplicationService;
import com.cy.onepush.project.domain.Module;
import com.cy.onepush.project.domain.Project;
import com.cy.onepush.project.interfaces.ProjectModulesApi;
import com.cy.onepush.project.interfaces.assembly.ProjectModuleVOAssembly;
import com.cy.onepush.project.interfaces.params.CreateModuleParams;
import com.cy.onepush.project.interfaces.params.SearchModuleParams;
import com.cy.onepush.project.interfaces.params.UpdateModuleParams;
import com.cy.onepush.project.interfaces.vo.ProjectModuleVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ProjectModulesApiImpl implements ProjectModulesApi {

    private final ProjectApplicationService projectApplicationService;

    @Override
    public PaginationResult<ProjectModuleVO> list(String projectCode, SearchModuleParams params) {
        final Project project = projectApplicationService.get(projectCode);
        final List<Module> modules = project.getModules();
        final PageInfo<Module> pageInfo = PageHelper.doSelectPageInMemory(params.getPageNum(), params.getPageSize(),
            () -> modules.stream().filter(module -> params.getName() == null || StringUtils.contains(module.getName(), params.getName())).collect(Collectors.toList()),
            Comparator.comparing(Module::getName));

        return PaginationResult.<ProjectModuleVO>paginationBuilder()
            .list(ProjectModuleVOAssembly.ASSEMBLY.vosFromDomains(projectCode, pageInfo.getList()))
            .pagination(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal())
            .build();
    }

    @Override
    public Result<String> add(String projectCode, CreateModuleParams params) {
        projectApplicationService.addModule(projectCode, params);
        return Result.<String>builder()
            .data(params.getCode())
            .build();
    }

    @Override
    public Result<Boolean> update(String projectCode, String moduleCode, UpdateModuleParams params) {
        projectApplicationService.updateModule(projectCode, moduleCode, params);
        return Result.<Boolean>builder()
            .data(Boolean.TRUE)
            .build();
    }

    @Override
    public Result<Boolean> delete(String projectCode, String moduleCode) {
        final boolean result = projectApplicationService.removeModule(projectCode, moduleCode);
        return Result.<Boolean>builder()
            .data(result)
            .build();
    }

}
