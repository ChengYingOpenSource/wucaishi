package com.cy.onepush.project.infrastructure.repository.mybatis.assembly;

import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.project.domain.Project;
import com.cy.onepush.project.infrastructure.repository.mybatis.bean.ProjectDatasourceDO;
import com.cy.onepush.project.interfaces.ProjectDataSourceApi;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProjectDataSourceDOAssembly {

    ProjectDataSourceDOAssembly ASSEMBLY = Mappers.getMapper(ProjectDataSourceDOAssembly.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "projectCode", source = "projectCode")
    @Mapping(target = "datasourceCode", source = "dataSourceId.id")
    @Mapping(target = "gmtCreated", source = "date")
    @Mapping(target = "gmtModified", source = "date")
    ProjectDatasourceDO doFromDomain(String projectCode, DataSourceId dataSourceId, Date date);

    default List<ProjectDatasourceDO> dosFromDomain(Project project, Date date) {
        final String projectCode = project.getId().getId();

        return project.getDataSourceIds().stream()
            .map(dataSourceId -> ProjectDataSourceDOAssembly.ASSEMBLY.doFromDomain(projectCode, dataSourceId, date))
            .collect(Collectors.toList());
    }

}
