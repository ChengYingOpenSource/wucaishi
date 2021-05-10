package com.cy.onepush.datasource.domain;

import com.cy.onepush.common.framework.domain.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value(staticConstructor = "of")
@EqualsAndHashCode(callSuper = false)
public class DataSourceProperty extends ValueObject {

    String key;
    String value;

}
