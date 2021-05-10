package com.cy.onepush.gateway.domain;

import com.cy.onepush.common.exception.ResourceNotFoundException;
import lombok.Getter;

import java.util.Arrays;

public enum GatewayStatus {

    DRAFT(0),
    PUBLISHED(1);

    public static GatewayStatus of(int value) {
        return Arrays.stream(GatewayStatus.values())
            .filter(item -> item.getValue() == value)
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("the gateway status value %d is not supported", value));
    }

    @Getter
    private final int value;

    GatewayStatus(int value) {
        this.value = value;
    }

}
