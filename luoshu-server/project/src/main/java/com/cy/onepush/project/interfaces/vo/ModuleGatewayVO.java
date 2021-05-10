package com.cy.onepush.project.interfaces.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ModuleGatewayVO {

    private String project;
    private String app;
    private String apiKey;
    private String version;
    private String description;
    private String url;
    private Date createTime;
    private Date updateTime;
    private Integer status;

    private List<GatewayViewItem> dataViewers;

    @Data
    public static class GatewayViewItem {

        private String dataViewCode;
        private String dataViewType;
        private String dataSourceName;

    }

}
