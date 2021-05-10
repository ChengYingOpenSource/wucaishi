package com.cy.onepush.common.publishlanguage.gateway;

import com.cy.onepush.common.framework.publishedlanguage.AggregateId;

public class GatewayId extends AggregateId<String> {

    public static GatewayId of(String code) {
        return new GatewayId(code);
    }

    private final String code;

    protected GatewayId(String code) {
        super(code);

        this.code = code;
    }

}
