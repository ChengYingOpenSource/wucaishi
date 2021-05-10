package com.cy.onepush.gateway.domain;

import com.cy.onepush.common.framework.domain.ValueObject;
import com.cy.onepush.common.publishlanguage.gateway.GatewayId;
import com.cy.onepush.common.publishlanguage.version.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Value(staticConstructor = "of")
public class GatewayIdWithVersion extends ValueObject {

    GatewayId gatewayId;
    Version version;

    @Override
    public String toString() {
        return String.format("%s_%s", gatewayId.getId(), version.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        GatewayIdWithVersion that = (GatewayIdWithVersion) o;

        return new EqualsBuilder().append(gatewayId, that.gatewayId).append(version, that.version).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(gatewayId).append(version).toHashCode();
    }
}
