package com.cy.onepush.common.framework.infrastructure.repository.page;

import lombok.Data;

import java.util.List;

/**
 * page info
 *
 * @author WhatAKitty
 * @date 2020-12-23
 */
@Data
public class PageInfo<T> {

    private final int pageNum;
    private final int pageSize;
    private long total;

    private List<T> list;

}
