package com.cy.onepush.project.infrastructure.repository.mybatis.assembly;

import com.cy.onepush.common.publishlanguage.gateway.GatewayId;
import com.cy.onepush.gateway.domain.GatewayIdWithVersion;
import com.cy.onepush.project.domain.Module;
import com.cy.onepush.project.infrastructure.repository.mybatis.bean.ProjectModuleGatewayDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProjectModuleGatewayDOAssembly {

    ProjectModuleGatewayDOAssembly ASSEMBLY = Mappers.getMapper(ProjectModuleGatewayDOAssembly.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataInterfaceCode", source = "gatewayIdWithVersion.gatewayId.id")
    @Mapping(target = "version", source = "gatewayIdWithVersion.version.id")
    @Mapping(target = "projectCode", source = "projectCode")
    @Mapping(target = "moduleCode", source = "moduleCode")
    @Mapping(target = "gmtCreated", source = "date")
    @Mapping(target = "gmtModified", source = "date")
    ProjectModuleGatewayDO doFromDomain(String projectCode, String moduleCode, GatewayIdWithVersion gatewayIdWithVersion, Date date);

    default List<ProjectModuleGatewayDO> dosFromDomain(String projectCode, Module module, Date date) {
        final String moduleCode = module.getId();
        return module.getGateways().parallelStream()
            .map(gatewayIdWithVersion -> ProjectModuleGatewayDOAssembly.ASSEMBLY.doFromDomain(projectCode, moduleCode, gatewayIdWithVersion, date))
            .collect(Collectors.toList());
    }

    default List<ProjectModuleGatewayDO> dosFromDomains(String projectCode, Collection<Module> modules, Date date) {
        return modules.parallelStream()
            .map(module -> ProjectModuleGatewayDOAssembly.ASSEMBLY.dosFromDomain(projectCode, module, date))
            .flatMap(Collection::parallelStream)
            .collect(Collectors.toList());
    }

}
