package com.cy.onepush.datapackager.infrastructure.repository.mybatis.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * ep_data_packager_view
 * @author 
 */
public class DataPackagerViewDO implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 数据组装服务编码
     */
    private String dataPackagerCode;

    /**
     * 版本
     */
    private String version;

    /**
     * 数据视图编码
     */
    private String dataViewCode;

    /**
     * 数据视图别名
     */
    private String dataViewAlias;

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

    public String getDataPackagerCode() {
        return dataPackagerCode;
    }

    public void setDataPackagerCode(String dataPackagerCode) {
        this.dataPackagerCode = dataPackagerCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDataViewCode() {
        return dataViewCode;
    }

    public void setDataViewCode(String dataViewCode) {
        this.dataViewCode = dataViewCode;
    }

    public String getDataViewAlias() {
        return dataViewAlias;
    }

    public void setDataViewAlias(String dataViewAlias) {
        this.dataViewAlias = dataViewAlias;
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
}