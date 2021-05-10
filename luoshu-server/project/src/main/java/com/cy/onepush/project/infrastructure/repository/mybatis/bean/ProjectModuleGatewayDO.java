package com.cy.onepush.project.infrastructure.repository.mybatis.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * ep_project_module_gateway
 * @author 
 */
public class ProjectModuleGatewayDO implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 模块编码
     */
    private String moduleCode;

    /**
     * 接口编码
     */
    private String dataInterfaceCode;

    /**
     * 接口版本
     */
    private String version;

    /**
     * 创建时间
     */
    private Date gmtCreated;

    /**
     * 修改时间
     */
    private Date gmtModified;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getDataInterfaceCode() {
        return dataInterfaceCode;
    }

    public void setDataInterfaceCode(String dataInterfaceCode) {
        this.dataInterfaceCode = dataInterfaceCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Date gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProjectModuleGatewayDO gatewayDO = (ProjectModuleGatewayDO) o;

        return new EqualsBuilder().append(projectCode, gatewayDO.projectCode)
            .append(moduleCode, gatewayDO.moduleCode)
            .append(dataInterfaceCode, gatewayDO.dataInterfaceCode)
            .append(version, gatewayDO.getVersion())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(projectCode).append(moduleCode).append(dataInterfaceCode).append(version).toHashCode();
    }
}