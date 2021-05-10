package com.cy.onepush.project.interfaces.assembly;

import com.cy.onepush.project.domain.Project;
import com.cy.onepush.project.interfaces.vo.ProjectVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper
public interface ProjectVOAssembly {

    ProjectVOAssembly ASSEMBLY = Mappers.getMapper(ProjectVOAssembly.class);

    @Mapping(target = "code", source = "id.id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "updateTime", source = "modifyTime")
    @Mapping(target = "createTime", source = "createTime")
    ProjectVO voFromDomain(Project project);

    List<ProjectVO> vosFromDomains(Collection<Project> collection);

}
