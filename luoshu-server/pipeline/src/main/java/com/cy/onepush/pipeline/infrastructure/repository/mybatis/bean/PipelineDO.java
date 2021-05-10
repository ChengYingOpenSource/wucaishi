package com.cy.onepush.pipeline.infrastructure.repository.mybatis.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * ep_pipeline
 * @author 
 */
public class PipelineDO implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 生成的接口编码
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

    /**
     * 创建人
     */
    private Long gmtCreator;

    /**
     * 修改人
     */
    private Long gmtModifier;

    /**
     * 暂存数据
     */
    private byte[] data;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}