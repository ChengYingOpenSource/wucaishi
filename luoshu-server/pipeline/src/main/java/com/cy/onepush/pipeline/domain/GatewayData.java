package com.cy.onepush.pipeline.domain;

import com.cy.onepush.common.framework.domain.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class GatewayData extends ValueObject {

    String gatewayCode;
    String gatewayName;
    String gatewayMethod;
    String gatewayUri;
    String gatewayDescription;
    String version;

}
