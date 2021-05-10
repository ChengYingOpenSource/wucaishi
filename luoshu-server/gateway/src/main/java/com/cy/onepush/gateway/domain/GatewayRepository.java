package com.cy.onepush.gateway.domain;

import com.cy.onepush.common.framework.infrastructure.repository.page.PageInfo;
import com.cy.onepush.common.publishlanguage.gateway.GatewayId;

import java.util.Collection;

public interface GatewayRepository {

    Collection<Gateway> all();

    Collection<Gateway> allPublished();

    PageInfo<Gateway> pagination(String moduleCode, String name, Integer status, int pageNum, int pageSize);

    Gateway get(GatewayIdWithVersion gatewayIdWithVersion);

    void add(Gateway gateway);

    void update(Gateway gateway);

    void delete(Gateway gateway);

}
