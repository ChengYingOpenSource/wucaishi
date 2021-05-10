package com.cy.onepush.project.domain;

import com.cy.onepush.common.exception.ResourceNotFoundException;
import com.cy.onepush.common.framework.domain.AbstractAggregateRoot;
import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.common.publishlanguage.gateway.GatewayId;
import com.cy.onepush.common.publishlanguage.project.ProjectId;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.gateway.domain.GatewayIdWithVersion;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 项目
 *
 * @author WhatAKitty
 * @version 0.1.0
 */
public class Project extends AbstractAggregateRoot<String> {

    private String name;
    private String description;
    private Date createTime;
    private Date modifyTime;

    private final List<Module> modules;

    private final Set<DataSourceId> dataSources;

    public Project(ProjectId id) {
        this(id, null);
    }

    public Project(ProjectId id, String name) {
        super(id);
        this.name = name;
        this.modules = new ArrayList<>(16);
        this.dataSources = new HashSet<>(16);
        this.createTime = modifyTime = new Date();
    }

    @Override
    public ProjectId getId() {
        return ProjectId.of(super.getId().getId());
    }

    public void addModule(String moduleCode, String moduleName, String description) {
        this.addModule(moduleCode, moduleName, description, Lists.newArrayListWithCapacity(8));

        this.modifyTime = new Date();
    }

    public void addModule(String moduleCode, String moduleName, String description, Collection<GatewayIdWithVersion> gatewayIds) {
        // create module
        final Module module = new Module(moduleCode, moduleName, gatewayIds);
        module.modifyDescription(description);
        this.modules.add(module);

        this.modifyTime = new Date();
    }

    public void addModule(Module module) {
        this.modules.add(module);
        this.modifyTime = new Date();
    }

    public void updateModule(String moduleCode, String moduleName, String description) {
        final Module module = this.modules.stream().filter(item -> item.getId().equals(moduleCode)).findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("the resource module %s in project %s not found", moduleCode, getId().getId()));

        module.modifyName(moduleName);
        module.modifyDescription(description);

        this.modifyTime = new Date();
    }

    public boolean removeModule(String moduleCode) {
        return this.modules.removeIf(item -> item.getId().equals(moduleCode) && item.delete());
    }

    public void addGateway(String moduleCode, GatewayIdWithVersion gatewayId) {
        final Module module = this.modules.stream().filter(item -> item.getId().equals(moduleCode)).findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("the resource module %s in project %s not found", moduleCode, getId().getId()));

        module.addGateway(gatewayId);

        this.modifyTime = new Date();
    }

    public void removeGateway(String moduleCode, GatewayIdWithVersion gatewayId) {
        final Module module = this.modules.stream().filter(item -> item.getId().equals(moduleCode)).findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("the resource module %s in project %s not found", moduleCode, getId().getId()));

        module.removeGateway(gatewayId);

        this.modifyTime = new Date();
    }

    public void addDataSource(DataSourceId dataSourceId) {
        this.dataSources.add(dataSourceId);

        this.modifyTime = new Date();
    }

    public void removeDataSource(DataSourceId dataSourceId) {
        this.dataSources.remove(dataSourceId);

        this.modifyTime = new Date();
    }

    public void modifyName(String name) {
        this.name = name;
        this.modifyTime = new Date();
    }

    public void modifyDescription(String description) {
        this.description = description;
        this.modifyTime = new Date();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Module> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public Set<DataSourceId> getDataSourceIds() {
        return Collections.unmodifiableSet(dataSources);
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public boolean delete() {
        this.modifyTime = new Date();

        return CollectionUtils.isEmpty(modules);
    }

}
