package com.cy.onepush.project.domain;

import com.cy.onepush.common.framework.infrastructure.repository.page.PageInfo;
import com.cy.onepush.common.publishlanguage.project.ProjectId;

import java.util.Optional;

public interface ProjectRepository {

    PageInfo<Project> pagination(int pageNum, int pageSize, String name);

    Optional<Project> get(ProjectId projectId);

    Optional<Project> getByModuleId(String moduleId);

    void add(Project project);

    void update(Project project);

    void delete(Project project);

}
