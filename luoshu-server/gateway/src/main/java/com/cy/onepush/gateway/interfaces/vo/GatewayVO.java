package com.cy.onepush.gateway.interfaces.vo;

import lombok.Data;

import java.util.List;

@Data
public class GatewayVO {

    private String project;
    private String app;
    private String apiKey;
    private String version;
    private String description;
    private String url;
    private String createTime;
    private String updateTime;
    private List<GatewayDataViewerDTO> dataViewers;

    @Data
    public static class GatewayDataViewerDTO {

        private String dataViewCode;
        private String dataViewType;
        private String dataSourceName;

    }

}
