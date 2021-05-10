package com.cy.onepush.datapackager.infrastructure.repository.mybatis.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * ep_data_packager
 * @author 
 */
public class DataPackagerDO implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 编码
     */
    private String code;

    /**
     * 组装服务名称
     */
    private String name;

    /**
     * 请求数据结构编码
     */
    private String reqDatastructureCode;

    /**
     * 响应数据结构编码
     */
    private String respDatastructureCode;

    /**
     * 版本
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

    public String getReqDatastructureCode() {
        return reqDatastructureCode;
    }

    public void setReqDatastructureCode(String reqDatastructureCode) {
        this.reqDatastructureCode = reqDatastructureCode;
    }

    public String getRespDatastructureCode() {
        return respDatastructureCode;
    }

    public void setRespDatastructureCode(String respDatastructureCode) {
        this.respDatastructureCode = respDatastructureCode;
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
}