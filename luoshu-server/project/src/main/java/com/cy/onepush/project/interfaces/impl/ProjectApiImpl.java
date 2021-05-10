package com.cy.onepush.project.interfaces.impl;

import com.cy.onepush.common.framework.infrastructure.repository.page.PageInfo;
import com.cy.onepush.common.framework.infrastructure.web.PaginationResult;
import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.project.application.ProjectApplicationService;
import com.cy.onepush.project.domain.Project;
import com.cy.onepush.project.interfaces.ProjectApi;
import com.cy.onepush.project.interfaces.assembly.ProjectVOAssembly;
import com.cy.onepush.project.interfaces.params.CreateProjectParams;
import com.cy.onepush.project.interfaces.params.SearchProjectParams;
import com.cy.onepush.project.interfaces.params.UpdateProjectParams;
import com.cy.onepush.project.interfaces.vo.ProjectVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProjectApiImpl implements ProjectApi {

    private final ProjectApplicationService projectApplicationService;

    @Override
    public PaginationResult<ProjectVO> list(SearchProjectParams params) {
        final PageInfo<Project> pageInfo = projectApplicationService.pagination(params);

        return PaginationResult.<ProjectVO>paginationBuilder()
            .list(ProjectVOAssembly.ASSEMBLY.vosFromDomains(pageInfo.getList()))
            .pagination(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal())
            .build();
    }

    @Override
    public Result<String> create(CreateProjectParams createProjectParams) {
        final Project project = projectApplicationService.createProject(createProjectParams);
        return Result.<String>builder().success(project.getId().getId()).build();
    }

    @Override
    public Result<Boolean> update(String projectCode, UpdateProjectParams params) {
        projectApplicationService.updateProject(projectCode, params);
        return Result.<Boolean>builder().success(true).build();
    }

    @Override
    public Result<Boolean> delete(String projectCode) {
        final boolean result = projectApplicationService.deleteProject(projectCode);
        return Result.<Boolean>builder().success(result).build();
    }

}
