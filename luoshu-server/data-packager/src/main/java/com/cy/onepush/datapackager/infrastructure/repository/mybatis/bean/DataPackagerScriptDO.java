package com.cy.onepush.datapackager.infrastructure.repository.mybatis.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * ep_data_packager_script
 * @author 
 */
public class DataPackagerScriptDO implements Serializable {
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
     * 脚本类型
     */
    private String scriptType;

    /**
     * 创建时间
     */
    private Date gmtCreated;

    /**
     * 修改时间
     */
    private Date gmtModified;

    /**
     * 脚本内容
     */
    private String scriptContent;

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

    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
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

    public String getScriptContent() {
        return scriptContent;
    }

    public void setScriptContent(String scriptContent) {
        this.scriptContent = scriptContent;
    }
}