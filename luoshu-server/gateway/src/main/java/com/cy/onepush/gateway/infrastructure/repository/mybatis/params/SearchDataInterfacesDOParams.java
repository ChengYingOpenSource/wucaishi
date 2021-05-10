package com.cy.onepush.gateway.infrastructure.repository.mybatis.params;

import lombok.Data;

@Data
public class SearchDataInterfacesDOParams {

    private String moduleCode;
    private String name;
    private String version;
    private Integer status;

}
