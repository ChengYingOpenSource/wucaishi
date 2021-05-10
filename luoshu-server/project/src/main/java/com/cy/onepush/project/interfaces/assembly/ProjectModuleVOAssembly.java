package com.cy.onepush.project.interfaces.assembly;

import com.cy.onepush.project.domain.Module;
import com.cy.onepush.project.interfaces.vo.ProjectModuleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProjectModuleVOAssembly {

    ProjectModuleVOAssembly ASSEMBLY = Mappers.getMapper(ProjectModuleVOAssembly.class);

    @Mapping(target = "name", source = "module.name")
    @Mapping(target = "moduleCode", source = "module.id")
    @Mapping(target = "projectCode", source = "projectCode")
    @Mapping(target = "description", source = "module.description")
    @Mapping(target = "createTime", source = "module.createTime")
    @Mapping(target = "updateTime", source = "module.modifyTime")
    ProjectModuleVO voFromDomain(String projectCode, Module module);

    default List<ProjectModuleVO> vosFromDomains(String projectCode, Collection<Module> modules) {
        return modules.parallelStream()
            .map(module -> ProjectModuleVOAssembly.ASSEMBLY.voFromDomain(projectCode, module))
            .collect(Collectors.toList());
    }

}
