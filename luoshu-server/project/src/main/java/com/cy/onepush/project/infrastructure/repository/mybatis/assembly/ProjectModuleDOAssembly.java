package com.cy.onepush.project.infrastructure.repository.mybatis.assembly;

import com.cy.onepush.common.publishlanguage.gateway.GatewayId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.gateway.domain.GatewayIdWithVersion;
import com.cy.onepush.project.domain.Module;
import com.cy.onepush.project.infrastructure.repository.mybatis.bean.ProjectModuleDO;
import com.cy.onepush.project.infrastructure.repository.mybatis.bean.ProjectModuleGatewayDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface ProjectModuleDOAssembly {

    ProjectModuleDOAssembly ASSEMBLY = Mappers.getMapper(ProjectModuleDOAssembly.class);

    default Module domainFromDo(ProjectModuleDO projectModuleDO, List<ProjectModuleGatewayDO> moduleGatewayDOS) {
        // get gatewayIds
        final Set<GatewayIdWithVersion> gatewayIdWithVersions = moduleGatewayDOS.parallelStream()
            .map(item -> GatewayIdWithVersion.of(GatewayId.of(item.getDataInterfaceCode()), Version.of(item.getVersion())))
            .collect(Collectors.toSet());

        // create
        final Module module = new Module(projectModuleDO.getCode(), projectModuleDO.getName(), gatewayIdWithVersions);
        module.modifyDescription(projectModuleDO.getDescription());
        module.setCreateTime(projectModuleDO.getGmtCreated());
        module.setModifyTime(projectModuleDO.getGmtModified());

        return module;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", source = "module.id")
    @Mapping(target = "projectCode", source = "projectCode")
    @Mapping(target = "gmtCreated", source = "date")
    @Mapping(target = "gmtCreator", expression = "java( -1L )")
    @Mapping(target = "gmtModified", source = "date")
    @Mapping(target = "gmtModifier", expression = "java( -1L )")
    ProjectModuleDO doFromDomain(String projectCode, Module module, Date date);

    default List<ProjectModuleDO> dosFromDomain(String projectCode, Collection<Module> collection, Date date) {
        return collection.parallelStream()
            .map(item -> ProjectModuleDOAssembly.ASSEMBLY.doFromDomain(projectCode, item, date))
            .collect(Collectors.toList());
    }

}
