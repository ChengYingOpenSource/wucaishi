package com.cy.onepush.gateway.domain;

import com.cy.onepush.common.framework.domain.AbstractAggregateRoot;
import com.cy.onepush.common.publishlanguage.executioncontext.ExecutionContextId;
import com.cy.onepush.common.publishlanguage.gateway.GatewayId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.common.utils.URLAppender;
import com.cy.onepush.datapackager.domain.DataPackager;
import com.cy.onepush.execution.context.domain.ExecutionContext;
import com.cy.onepush.gateway.domain.event.GatewayOfflineEvent;
import com.cy.onepush.gateway.domain.event.GatewayPublishedEvent;
import com.sun.el.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class Gateway extends AbstractAggregateRoot<String> {

    private final String name;
    private final Router router;
    private final Version version;
    private DataPackager dataPackager;
    private GatewayStatus gatewayStatus;
    private String description;
    private Date createTime;
    private Date modifyTime;

    public Gateway(GatewayId id, String name, String method, String uri, Version version) {
        super(id);
        this.name = name;
        this.gatewayStatus = GatewayStatus.DRAFT;
        this.version = version;

        if (StringUtils.isBlank(uri)) {
            // generate by system
            // rule: /${gatewayCode}/${version_snake_case}
            // TODO version not specific
            uri = URLAppender.getInstance().append(getId().getId()).append("0_1_0").build();
        }
        if (StringUtils.isBlank(method)) {
            method = RequestMethod.POST.name();
        }
        this.router = new Router();
        this.router.defineUri(method, uri);

        this.createTime = this.modifyTime = new Date();
    }

    @Override
    public GatewayId getId() {
        return GatewayId.of(super.getId().getId());
    }

    public GatewayIdWithVersion getIdWithVersion() {
        return GatewayIdWithVersion.of(getId(), getGatewayVersion());
    }

    public void bindDataPackager(DataPackager dataPackager) {
        this.dataPackager = dataPackager;
        this.modifyTime = new Date();
    }

    public Object execute(Map<String, Object> params) {
        // create execution context
        final ExecutionContext executionContext = new ExecutionContext(ExecutionContextId.of(UUID.randomUUID().toString()));
        executionContext.setParams(params);

        // enter context
        executionContext.enterContext();

        try {
            // execute data packager service
            dataPackager.execute(executionContext);
        } finally {
            executionContext.exitContext();
        }

        return executionContext.getResult();
    }

    public void publish() {
        this.gatewayStatus = GatewayStatus.PUBLISHED;
        this.modifyTime = new Date();

        // publish event
        publishEvent(new GatewayPublishedEvent(this));
    }

    public void offline() {
        this.gatewayStatus = GatewayStatus.DRAFT;
        this.modifyTime = new Date();

        // publish event
        publishEvent(new GatewayOfflineEvent(this));
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return this.router.getUri().toString();
    }

    public String getMethod() {
        return this.router.getMethod().name();
    }

    public DataPackager getDataPackager() {
        return dataPackager;
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

    public void setGatewayStatus(GatewayStatus gatewayStatus) {
        this.gatewayStatus = gatewayStatus;
    }

    public GatewayStatus getGatewayStatus() {
        return gatewayStatus;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Gateway createNewVersion(String newVersion) {
        final Gateway newGateway = new Gateway(getId(), name, getMethod(), getUri(), Version.of(newVersion));
        newGateway.gatewayStatus = this.gatewayStatus;
        newGateway.bindDataPackager(getDataPackager());
        return newGateway;
    }

    public Version getGatewayVersion() {
        return version;
    }

}
