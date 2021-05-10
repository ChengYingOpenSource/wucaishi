package com.cy.onepush.project.application;

import com.cy.onepush.common.exception.Asserts;
import com.cy.onepush.common.framework.infrastructure.repository.page.PageInfo;
import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.common.publishlanguage.gateway.GatewayId;
import com.cy.onepush.common.publishlanguage.project.ProjectId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.gateway.domain.GatewayIdWithVersion;
import com.cy.onepush.project.domain.Project;
import com.cy.onepush.project.domain.ProjectRepository;
import com.cy.onepush.project.interfaces.params.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectApplicationService {

    private final ProjectRepository projectRepository;

    public PageInfo<Project> pagination(SearchProjectParams params) {
        return projectRepository.pagination(params.getPageNum(), params.getPageSize(), params.getName());
    }

    public Project get(String projectId) {
        final Optional<Project> projectOptional = projectRepository.get(ProjectId.of(projectId));
        return projectOptional.orElseThrow(() -> Asserts.notFoundFail(projectId));
    }

    public Project getByModuleCode(String moduleId) {
        final Optional<Project> projectOptional = projectRepository.getByModuleId(moduleId);
        return projectOptional.orElseThrow(() -> Asserts.notFoundFail(moduleId));
    }

    @Transactional(rollbackFor = Throwable.class)
    public Project createProject(CreateProjectParams params) {
        // create project
        final Project project = new Project(ProjectId.of(params.getCode()), params.getName());
        project.modifyDescription(params.getDescription());

        // add into repository
        projectRepository.add(project);

        return project;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateProject(String projectId, UpdateProjectParams params) {
        final Optional<Project> projectOptional = projectRepository.get(ProjectId.of(projectId));
        final Project project = projectOptional.orElseThrow(() -> Asserts.notFoundFail(projectId));

        project.modifyName(params.getName());
        project.modifyDescription(params.getDescription());

        // update
        projectRepository.update(project);
    }

    @Transactional(rollbackFor = Throwable.class)
    public boolean deleteProject(String projectId) {
        final Optional<Project> projectOptional = projectRepository.get(ProjectId.of(projectId));
        final Project project = projectOptional.orElseThrow(() -> Asserts.notFoundFail(projectId));

        if (!project.delete()) {
            return Boolean.FALSE;
        }

        projectRepository.delete(project);
        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void addModule(String projectId, CreateModuleParams params) {
        final Optional<Project> projectOptional = projectRepository.get(ProjectId.of(projectId));
        final Project project = projectOptional.orElseThrow(() -> Asserts.notFoundFail(projectId));

        project.addModule(params.getCode(), params.getName(), params.getDescription());

        // update
        projectRepository.update(project);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateModule(String projectId, String moduleId, UpdateModuleParams params) {
        final Optional<Project> projectOptional = projectRepository.get(ProjectId.of(projectId));
        final Project project = projectOptional.orElseThrow(() -> Asserts.notFoundFail(projectId));

        project.updateModule(moduleId, params.getName(), params.getDescription());

        // update
        projectRepository.update(project);
    }

    @Transactional(rollbackFor = Throwable.class)
    public boolean removeModule(String projectId, String moduleId) {
        final Optional<Project> projectOptional = projectRepository.get(ProjectId.of(projectId));
        final Project project = projectOptional.orElseThrow(() -> Asserts.notFoundFail(projectId));

        if (!project.removeModule(moduleId)) {
            return false;
        }

        // update
        projectRepository.update(project);
        return true;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void addGateway(String projectId, String moduleId, String gatewayId, String version) {
        final Optional<Project> projectOptional = projectRepository.get(ProjectId.of(projectId));
        final Project project = projectOptional.orElseThrow(() -> Asserts.notFoundFail(projectId));

        project.addGateway(moduleId, GatewayIdWithVersion.of(GatewayId.of(gatewayId), Version.of(version)));

        // update
        projectRepository.update(project);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void removeGateway(String projectId, String moduleId, String gatewayId, String version) {
        final Optional<Project> projectOptional = projectRepository.get(ProjectId.of(projectId));
        final Project project = projectOptional.orElseThrow(() -> Asserts.notFoundFail(projectId));

        project.removeGateway(moduleId, GatewayIdWithVersion.of(GatewayId.of(gatewayId), Version.of(version)));

        // update
        projectRepository.update(project);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void addDataSource(String projectId, String dataSourceId) {
        final Optional<Project> projectOptional = projectRepository.get(ProjectId.of(projectId));
        final Project project = projectOptional.orElseThrow(() -> Asserts.notFoundFail(projectId));

        project.addDataSource(DataSourceId.of(dataSourceId));

        // update
        projectRepository.update(project);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void removeDataSource(String projectId, String dataSourceId) {
        final Optional<Project> projectOptional = projectRepository.get(ProjectId.of(projectId));
        final Project project = projectOptional.orElseThrow(() -> Asserts.notFoundFail(projectId));

        project.removeDataSource(DataSourceId.of(dataSourceId));

        // update
        projectRepository.update(project);
    }

}
