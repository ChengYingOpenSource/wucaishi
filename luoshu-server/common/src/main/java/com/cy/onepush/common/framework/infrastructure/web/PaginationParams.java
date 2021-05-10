package com.cy.onepush.common.framework.infrastructure.web;

import lombok.Data;

@Data
public class PaginationParams {

    private Integer pageSize = 10;
    private Integer pageNum = 1;

}
