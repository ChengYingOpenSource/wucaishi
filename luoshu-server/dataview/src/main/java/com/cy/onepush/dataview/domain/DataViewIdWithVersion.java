package com.cy.onepush.dataview.domain;

import com.cy.onepush.common.framework.domain.ValueObject;
import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.common.publishlanguage.version.Version;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Value(staticConstructor = "of")
public class DataViewIdWithVersion extends ValueObject {

    DataViewId dataViewId;
    Version version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DataViewIdWithVersion that = (DataViewIdWithVersion) o;

        return new EqualsBuilder().append(dataViewId, that.dataViewId).append(version, that.version).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(dataViewId).append(version).toHashCode();
    }
}
