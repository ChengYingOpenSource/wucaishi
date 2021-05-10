package com.cy.onepush.gateway.interfaces.params;

import com.cy.onepush.common.framework.infrastructure.web.PaginationParams;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchGatewayParams extends PaginationParams {

    private String app;
    private String name;
    private Integer status;
    private String version;

}
