package com.cy.onepush.pipeline.domain;

import com.cy.onepush.common.framework.domain.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Map;

@Value
@EqualsAndHashCode(callSuper = true)
public class DataSourceData extends ValueObject {

    String dataSourceCode;
    String dataSourceName;
    String dataSourceType;
    Map<String, String> dataSourceProperties;

}
