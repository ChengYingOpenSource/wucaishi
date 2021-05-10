package com.cy.onepush.project.infrastructure.repository;

import com.cy.onepush.common.framework.infrastructure.repository.page.PageHelper;
import com.cy.onepush.common.framework.infrastructure.repository.page.PageInfo;
import com.cy.onepush.common.publishlanguage.project.ProjectId;
import com.cy.onepush.project.domain.Project;
import com.cy.onepush.project.domain.ProjectRepository;
import com.cy.onepush.project.infrastructure.repository.mybatis.assembly.ProjectDOAssembly;
import com.cy.onepush.project.infrastructure.repository.mybatis.assembly.ProjectDataSourceDOAssembly;
import com.cy.onepush.project.infrastructure.repository.mybatis.assembly.ProjectModuleDOAssembly;
import com.cy.onepush.project.infrastructure.repository.mybatis.assembly.ProjectModuleGatewayDOAssembly;
import com.cy.onepush.project.infrastructure.repository.mybatis.bean.ProjectDO;
import com.cy.onepush.project.infrastructure.repository.mybatis.bean.ProjectDatasourceDO;
import com.cy.onepush.project.infrastructure.repository.mybatis.bean.ProjectModuleDO;
import com.cy.onepush.project.infrastructure.repository.mybatis.bean.ProjectModuleGatewayDO;
import com.cy.onepush.project.infrastructure.repository.mybatis.mapper.ProjectDatasourceMapper;
import com.cy.onepush.project.infrastructure.repository.mybatis.mapper.ProjectMapper;
import com.cy.onepush.project.infrastructure.repository.mybatis.mapper.ProjectModuleGatewayMapper;
import com.cy.onepush.project.infrastructure.repository.mybatis.mapper.ProjectModuleMapper;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * project repository
 *
 * @author WhatAKitty
 * @date 2020-12-23
 * @since 0.1.0
 */
@Component
@RequiredArgsConstructor
public class RdsProjectRepository implements ProjectRepository {

    private final ProjectMapper projectMapper;
    private final ProjectModuleMapper projectModuleMapper;
    private final ProjectModuleGatewayMapper projectModuleGatewayMapper;
    private final ProjectDatasourceMapper projectDatasourceMapper;

    @Override
    public PageInfo<Project> pagination(int pageNum, int pageSize, String name) {
        // pagination the project table
        final PageInfo<ProjectDO> pageInfo = PageHelper.doSelectPage(pageNum, pageSize, () -> {
            projectMapper.selectAll(name);
        });

        if (CollectionUtils.isEmpty(pageInfo.getList())) {
            // wrap to empty page
            final PageInfo<Project> result = new PageInfo<>(pageInfo.getPageNum(), pageInfo.getPageSize());
            result.setList(Lists.newArrayListWithCapacity(0));
            result.setTotal(pageInfo.getTotal());
            return result;
        }

        // get project codes
        final Set<String> projectCodes = pageInfo.getList().stream().map(ProjectDO::getCode).collect(Collectors.toSet());

        // get related data
        final Map<String, List<ProjectModuleDO>> moduleGroup = projectModuleMapper.selectByProjectCodes(projectCodes).stream()
            .collect(Collectors.groupingBy(ProjectModuleDO::getProjectCode));
        final Map<String, List<ProjectModuleGatewayDO>> gatewayGroup = projectModuleGatewayMapper.selectByProjectCodes(projectCodes).stream()
            .collect(Collectors.groupingBy(ProjectModuleGatewayDO::getProjectCode));
        final Map<String, List<ProjectDatasourceDO>> dataSourceGroup = projectDatasourceMapper.selectByProjectCodes(projectCodes).stream()
            .collect(Collectors.groupingBy(ProjectDatasourceDO::getProjectCode));

        // transfer to domain list
        final List<Project> list = pageInfo.getList().stream()
            .map(projectDO -> {
                final String projectDOCode = projectDO.getCode();
                final List<ProjectModuleDO> projectModuleDOS = moduleGroup.get(projectDOCode);
                final List<ProjectModuleGatewayDO> projectModuleGatewayDOS = gatewayGroup.get(projectDOCode);
                final List<ProjectDatasourceDO> projectDatasourceDOS = dataSourceGroup.get(projectDOCode);

                return ProjectDOAssembly.ASSEMBLY.domainFromDo(projectDO, projectModuleDOS, projectModuleGatewayDOS, projectDatasourceDOS);
            })
            .collect(Collectors.toList());

        // wrap to page info
        final PageInfo<Project> result = new PageInfo<>(pageInfo.getPageNum(), pageInfo.getPageSize());
        result.setList(list);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    @Override
    public Optional<Project> get(ProjectId projectId) {
        final String projectCode = projectId.getId();

        final ProjectDO projectDO = projectMapper.selectByCode(projectCode);
        if (projectDO == null) {
            return Optional.empty();
        }

        final List<ProjectModuleDO> projectModuleDOS = projectModuleMapper.selectByProjectCode(projectCode);
        final List<ProjectModuleGatewayDO> projectModuleGatewayDOS = projectModuleGatewayMapper.selectByProjectCode(projectCode);
        final List<ProjectDatasourceDO> projectDatasourceDOS = projectDatasourceMapper.selectByProjectCode(projectCode);

        return Optional.of(ProjectDOAssembly.ASSEMBLY.domainFromDo(projectDO, projectModuleDOS, projectModuleGatewayDOS, projectDatasourceDOS));
    }

    @Override
    public Optional<Project> getByModuleId(String moduleId) {
        final ProjectModuleDO projectModuleDO = projectModuleMapper.selectByCode(moduleId);
        if (projectModuleDO == null) {
            return Optional.empty();
        }

        final ProjectDO projectDO = projectMapper.selectByCode(projectModuleDO.getProjectCode());
        if (projectDO == null) {
            return Optional.empty();
        }

        final List<ProjectModuleGatewayDO> projectModuleGatewayDOS = projectModuleGatewayMapper.selectByProjectCode(projectModuleDO.getProjectCode());
        final List<ProjectDatasourceDO> projectDatasourceDOS = projectDatasourceMapper.selectByProjectCode(projectModuleDO.getProjectCode());

        return Optional.of(ProjectDOAssembly.ASSEMBLY.domainFromDo(projectDO, Lists.newArrayList(projectModuleDO), projectModuleGatewayDOS, projectDatasourceDOS));
    }

    @Override
    public void add(Project project) {
        final Date date = new Date();
        final String projectCode = project.getId().getId();

        final ProjectDO projectDO = ProjectDOAssembly.ASSEMBLY.doFromDomain(project, date);
        projectMapper.insert(projectDO);

        final List<ProjectModuleDO> projectModuleDOS = ProjectModuleDOAssembly.ASSEMBLY.dosFromDomain(projectCode, project.getModules(), date);
        if (CollectionUtils.isEmpty(projectModuleDOS)) {
            return;
        }
        projectModuleMapper.batchInsertOrUpdate(projectModuleDOS);

        final List<ProjectModuleGatewayDO> projectModuleGatewayDOS = ProjectModuleGatewayDOAssembly.ASSEMBLY.dosFromDomains(projectCode, project.getModules(), date);
        if (CollectionUtils.isNotEmpty(projectModuleGatewayDOS)) {
            projectModuleGatewayMapper.batchInsert(projectModuleGatewayDOS);
        }

        final List<ProjectDatasourceDO> projectDatasourceDOS = ProjectDataSourceDOAssembly.ASSEMBLY.dosFromDomain(project, date);
        if (CollectionUtils.isNotEmpty(projectDatasourceDOS)) {
            projectDatasourceMapper.batchInsertIgnore(projectDatasourceDOS);
        }
    }

    @Override
    public void update(Project project) {
        final Date date = new Date();
        final String projectCode = project.getId().getId();

        final ProjectDO projectDO = ProjectDOAssembly.ASSEMBLY.doFromDomain(project, date);
        projectMapper.updateByCode(projectDO);

        // lock while the same project updated
        synchronized (projectCode.intern()) {
            // get old project modules
            final Map<String, ProjectModuleDO> oldProjectModuleMap = projectModuleMapper.selectByProjectCode(projectCode).stream()
                .collect(Collectors.toMap(ProjectModuleDO::getCode, item -> item, (a, b) -> a));
            final Map<String, List<ProjectModuleGatewayDO>> oldModuleGroupGateways = projectModuleGatewayMapper.selectByProjectCode(projectCode).stream()
                .collect(Collectors.groupingBy(ProjectModuleGatewayDO::getModuleCode));
            final Map<String, ProjectDatasourceDO> oldProjectDatasourceMap = projectDatasourceMapper.selectByProjectCode(projectCode).stream()
                .collect(Collectors.toMap(ProjectDatasourceDO::getDatasourceCode, item -> item, (a, b) -> a));

            // the db related objects
            final List<ProjectModuleDO> insertOrUpdateModules = new ArrayList<>(project.getModules().size());
            final Set<ProjectModuleGatewayDO> insertModuleGateways = new HashSet<>(project.getModules().size() * 2);
            final Set<ProjectDatasourceDO> insertProjectDataSources = new HashSet<>(16);
            final List<ProjectModuleDO> deleteModules = new ArrayList<>(oldProjectModuleMap.values());
            final List<ProjectModuleGatewayDO> deleteModuleGateways = oldModuleGroupGateways.values().stream()
                .flatMap(Collection::stream).distinct().collect(Collectors.toList());
            final List<ProjectDatasourceDO> deleteProjectDataSources = new ArrayList<>(oldProjectDatasourceMap.values());

            // foreach the datasource and get insert or delete objects
            project.getDataSourceIds().forEach(dataSourceId -> {
                final ProjectDatasourceDO oldProjectDatasourceDO = oldProjectDatasourceMap.get(dataSourceId.getId());
                final ProjectDatasourceDO newProjectDataSourceDO = ProjectDataSourceDOAssembly.ASSEMBLY.doFromDomain(projectCode, dataSourceId, date);
                if (oldProjectDatasourceDO == null) {
                    // new one, insert it.
                    insertProjectDataSources.add(newProjectDataSourceDO);
                    return;
                }

                // no need for update, because of the unique key is {projectCode}-{dataSourceCode}
                // if exists just remove from delete list.
                deleteProjectDataSources.remove(oldProjectDatasourceDO);
            });

            // foreach the modules and get insert, update, delete objects
            project.getModules().forEach(module -> {
                final String moduleId = module.getId();

                // get new project module
                final ProjectModuleDO newProjectModuleDO = ProjectModuleDOAssembly.ASSEMBLY.doFromDomain(projectCode, module, date);
                // get new project module gateways
                final List<ProjectModuleGatewayDO> newProjectModuleGatewayDOS = ProjectModuleGatewayDOAssembly.ASSEMBLY.dosFromDomain(projectCode, module, date);

                final ProjectModuleDO oldProjectModuleDO;
                if ((oldProjectModuleDO = oldProjectModuleMap.get(moduleId)) == null) {
                    // not exists, a new module
                    insertOrUpdateModules.add(newProjectModuleDO);
                    insertModuleGateways.addAll(newProjectModuleGatewayDOS);
                    return;
                }
                deleteModules.remove(oldProjectModuleDO);

                // the project module not absolute equal. This is, the project module has been updated
                if (!oldProjectModuleDO.equals(newProjectModuleDO)) {
                    insertOrUpdateModules.add(newProjectModuleDO);
                }

                // the project module gateways not exists
                final List<ProjectModuleGatewayDO> oldProjectModuleGatewayDOS;
                if ((oldProjectModuleGatewayDOS = oldModuleGroupGateways.get(moduleId)) == null) {
                    // the module gateways is all new
                    insertModuleGateways.addAll(newProjectModuleGatewayDOS);
                    return;
                }
                deleteModuleGateways.removeAll(oldProjectModuleGatewayDOS);

                // the project module gateways not absolute equal. This is, the gateway has been updated
                // because of relation between gateway id and module, there is no choice for update but add or delete
                insertModuleGateways.addAll(CollectionUtils.subtract(newProjectModuleGatewayDOS, oldProjectModuleGatewayDOS));
            });

            // update into db
            if (CollectionUtils.isNotEmpty(insertProjectDataSources)) {
                projectDatasourceMapper.batchInsertIgnore(insertProjectDataSources);
            }
            if (CollectionUtils.isNotEmpty(deleteProjectDataSources)) {
                projectDatasourceMapper.batchDeleteByIds(deleteProjectDataSources.stream().map(ProjectDatasourceDO::getId).collect(Collectors.toSet()));
            }

            if (CollectionUtils.isNotEmpty(insertOrUpdateModules)) {
                projectModuleMapper.batchInsertOrUpdate(insertOrUpdateModules);
            }
            if (CollectionUtils.isNotEmpty(deleteModules)) {
                projectModuleMapper.batchDeleteByIds(deleteModules.stream().map(ProjectModuleDO::getId).collect(Collectors.toSet()));
            }
            if (CollectionUtils.isNotEmpty(insertModuleGateways)) {
                projectModuleGatewayMapper.batchInsert(insertModuleGateways);
            }
            if (CollectionUtils.isNotEmpty(deleteModuleGateways)) {
                projectModuleGatewayMapper.batchDelete(deleteModuleGateways.stream().map(ProjectModuleGatewayDO::getId).collect(Collectors.toSet()));
            }

        }
    }

    @Override
    public void delete(Project project) {
        final String projectCode = project.getId().getId();

        // lock while the same project updated
        synchronized (projectCode.intern()) {
            projectMapper.deleteByCode(projectCode);
        }
    }


}
