package com.cy.onepush.project.infrastructure.repository.mybatis.bean;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * ep_project_module
 *
 * @author
 */
public class ProjectModuleDO implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 项目编码
     */
    private String projectCode;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date gmtCreated;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 创建人
     */
    private Long gmtCreator;

    /**
     * 修改人
     */
    private Long gmtModifier;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getGmtCreator() {
        return gmtCreator;
    }

    public void setGmtCreator(Long gmtCreator) {
        this.gmtCreator = gmtCreator;
    }

    public Long getGmtModifier() {
        return gmtModifier;
    }

    public void setGmtModifier(Long gmtModifier) {
        this.gmtModifier = gmtModifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ProjectModuleDO that = (ProjectModuleDO) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder().append(code, that.code).append(name, that.name).append(projectCode, that.projectCode).append(description, that.description).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(code).append(name).append(projectCode).append(description).toHashCode();
    }
}