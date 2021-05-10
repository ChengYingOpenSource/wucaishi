package com.cy.onepush.project.domain;

import com.cy.onepush.common.framework.domain.AbstractEntity;
import com.cy.onepush.gateway.domain.GatewayIdWithVersion;
import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

public class Module extends AbstractEntity<String> {

    private String name;
    private String description;
    private final Collection<GatewayIdWithVersion> gateways;
    private Date createTime;
    private Date modifyTime;

    public Module(String id, String name) {
        this(id, name, Lists.newArrayListWithCapacity(8));
    }

    public Module(String id, String name, Collection<GatewayIdWithVersion> gateways) {
        this.setId(id);
        this.name = name;
        this.gateways = new ArrayList<>(gateways);
        this.createTime = modifyTime = new Date();
    }

    public void modifyName(String newName) {
        this.name = newName;
        this.modifyTime = new Date();
    }

    public void modifyDescription(String newDescription) {
        this.description = newDescription;
        this.modifyTime = new Date();
    }

    public void addGateway(GatewayIdWithVersion gatewayId) {
        this.gateways.add(gatewayId);
        this.modifyTime = new Date();
    }

    public void removeGateway(GatewayIdWithVersion gatewayId) {
        this.gateways.remove(gatewayId);
        this.modifyTime = new Date();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Collection<GatewayIdWithVersion> getGateways() {
        return Collections.unmodifiableCollection(gateways);
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

        return CollectionUtils.isEmpty(gateways);
    }
}
