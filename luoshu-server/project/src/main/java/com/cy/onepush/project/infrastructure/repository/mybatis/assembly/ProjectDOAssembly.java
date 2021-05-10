package com.cy.onepush.project.infrastructure.repository.mybatis.assembly;

import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.common.publishlanguage.gateway.GatewayId;
import com.cy.onepush.common.publishlanguage.project.ProjectId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.gateway.domain.GatewayIdWithVersion;
import com.cy.onepush.project.domain.Module;
import com.cy.onepush.project.domain.Project;
import com.cy.onepush.project.infrastructure.repository.mybatis.bean.ProjectDO;
import com.cy.onepush.project.infrastructure.repository.mybatis.bean.ProjectDatasourceDO;
import com.cy.onepush.project.infrastructure.repository.mybatis.bean.ProjectModuleDO;
import com.cy.onepush.project.infrastructure.repository.mybatis.bean.ProjectModuleGatewayDO;
import org.apache.commons.collections4.ListUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(imports = ProjectModuleDOAssembly.class)
public interface ProjectDOAssembly {

    ProjectDOAssembly ASSEMBLY = Mappers.getMapper(ProjectDOAssembly.class);

    default Project domainFromDo(ProjectDO projectDO, List<ProjectModuleDO> projectModuleDOS,
                                 List<ProjectModuleGatewayDO> projectModuleGatewayDOS,
                                 List<ProjectDatasourceDO> projectDatasourceDOS) {
        if (projectDO == null) {
            return null;
        }

        final String projectCode = projectDO.getCode();

        final Map<String, List<ProjectModuleGatewayDO>> moduleGroupGateways = ListUtils.emptyIfNull(projectModuleGatewayDOS).stream()
            .collect(Collectors.groupingBy(ProjectModuleGatewayDO::getModuleCode));

        final Project project = new Project(ProjectId.of(projectCode), projectDO.getName());
        project.modifyDescription(projectDO.getDescription());
        project.setCreateTime(projectDO.getGmtCreated());
        project.setModifyTime(projectDO.getGmtModified());
        ListUtils.emptyIfNull(projectModuleDOS).forEach(projectModuleDO -> {
            final List<ProjectModuleGatewayDO> moduleGatewayDOS = ListUtils.emptyIfNull(moduleGroupGateways.get(projectModuleDO.getCode()));

            final Module module = ProjectModuleDOAssembly.ASSEMBLY.domainFromDo(projectModuleDO, moduleGatewayDOS);
            project.addModule(module);
        });
        ListUtils.emptyIfNull(projectDatasourceDOS)
            .forEach(projectDatasourceDO -> project.addDataSource(DataSourceId.of(projectDatasourceDO.getDatasourceCode())));

        return project;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", source = "project.id.id")
    @Mapping(target = "name", source = "project.name")
    @Mapping(target = "description", source = "project.description")
    @Mapping(target = "gmtCreator", expression = "java( -1L )")
    @Mapping(target = "gmtCreated", source = "date")
    @Mapping(target = "gmtModifier", expression = "java( -1L )")
    @Mapping(target = "gmtModified", source = "date")
    ProjectDO doFromDomain(Project project, Date date);

}
