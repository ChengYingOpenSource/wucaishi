package com.cy.onepush.datasource.infrastructure.repository.mybatis.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * ep_data_source_v2
 * @author 
 */
public class DataSourceDO implements Serializable {
    /**
     * 自增，唯一id
     */
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 数据源名称，不能有中文，唯一
     */
    private String name;

    /**
     * 数据源类型
     */
    private String dsType;

    /**
     * 备注
     */
    private String remark;

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

    /**
     * 数据源配置，不同数据源内含字段不同
     */
    private String conf;

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

    public String getDsType() {
        return dsType;
    }

    public void setDsType(String dsType) {
        this.dsType = dsType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }
}