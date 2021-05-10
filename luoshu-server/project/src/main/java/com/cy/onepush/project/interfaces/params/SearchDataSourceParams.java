package com.cy.onepush.project.interfaces.params;

import com.cy.onepush.common.framework.infrastructure.web.PaginationParams;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchDataSourceParams extends PaginationParams {

    private String type;
    private String name;
    private String code;

}
