package com.cy.onepush.datapackager.domain;

import com.cy.onepush.common.framework.domain.ValueObject;
import com.cy.onepush.common.publishlanguage.datapackager.DataPackagerId;
import com.cy.onepush.common.publishlanguage.version.Version;
import lombok.Value;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Value(staticConstructor = "of")
public class DataPackagerIdWithVersion extends ValueObject {

    DataPackagerId dataPackagerId;
    Version version;

    @Override
    public String toString() {
        return String.format("%s_%s", dataPackagerId.getId(), version.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DataPackagerIdWithVersion that = (DataPackagerIdWithVersion) o;

        return new EqualsBuilder().append(dataPackagerId, that.dataPackagerId).append(version, that.version).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(dataPackagerId).append(version).toHashCode();
    }
}
