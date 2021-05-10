package com.cy.onepush.project.interfaces.impl;

import com.cy.onepush.common.framework.infrastructure.web.PaginationResult;
import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.datasource.application.DataSourceApplicationService;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.datasource.interfaces.assembly.DataSourceVOAssembly;
import com.cy.onepush.datasource.interfaces.vo.DataSourceVO;
import com.cy.onepush.project.application.ProjectApplicationService;
import com.cy.onepush.project.domain.Project;
import com.cy.onepush.project.interfaces.ProjectDataSourceApi;
import com.cy.onepush.project.interfaces.params.CreateDataSourceParams;
import com.cy.onepush.project.interfaces.params.SearchDataSourceParams;
import com.cy.onepush.project.interfaces.params.UpdateDataSourceParams;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ProjectDataSourceApiImpl implements ProjectDataSourceApi {

    private final ProjectApplicationService projectApplicationService;
    private final DataSourceApplicationService dataSourceApplicationService;

    @Override
    public PaginationResult<DataSourceVO> list(String projectCode, SearchDataSourceParams params) {
        final Set<String> dataSourceIds;
        if (StringUtils.isBlank(params.getCode())) {
            final Project project = projectApplicationService.get(projectCode);
            dataSourceIds = project.getDataSourceIds().parallelStream().map(DataSourceId::getId).collect(Collectors.toSet());
        } else {
            dataSourceIds = Sets.newHashSet(params.getCode());
        }
        final List<DataSource> all = dataSourceApplicationService.batchGet(dataSourceIds, params.getName(), params.getType());

        return PaginationResult.<DataSourceVO>paginationBuilder()
            .list(DataSourceVOAssembly.ASSEMBLY.vosFromDomains(all))
            .pagination(params.getPageNum(), all.size(), all.size())
            .build();
    }

    @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public Result<String> create(String projectCode, CreateDataSourceParams params) {
        // TODO move this logic into project application service
        final DataSource dataSource = dataSourceApplicationService.add(params.getDataSourceCode(), params.getDataSourceName(),
            params.getDataSourceType(), params.getDataSourceProperties());

        projectApplicationService.addDataSource(projectCode, params.getDataSourceCode());

        return Result.<String>builder().data(dataSource.getId().getId()).build();
    }

    @Override
    public Result<Boolean> update(String projectCode, String dataSourceCode, UpdateDataSourceParams params) {
        dataSourceApplicationService.update(dataSourceCode, params.getDataSourceName(),
            params.getDataSourceType(), params.getDataSourceProperties());

        return Result.<Boolean>builder().data(true).build();
    }

}
