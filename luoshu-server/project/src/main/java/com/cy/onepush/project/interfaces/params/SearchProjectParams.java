package com.cy.onepush.project.interfaces.params;

import com.cy.onepush.common.framework.infrastructure.web.PaginationParams;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchProjectParams extends PaginationParams {

    private String name;

}
