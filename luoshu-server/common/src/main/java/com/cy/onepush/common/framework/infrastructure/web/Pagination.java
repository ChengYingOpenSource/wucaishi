package com.cy.onepush.common.framework.infrastructure.web;

import lombok.Data;

@Data
public class Pagination {

    private int pageNum;
    private int pageSize;
    private long total;

}
