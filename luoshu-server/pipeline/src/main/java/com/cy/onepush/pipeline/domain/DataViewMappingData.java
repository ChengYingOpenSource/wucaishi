package com.cy.onepush.pipeline.domain;

import com.cy.onepush.common.framework.domain.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@EqualsAndHashCode(callSuper = true)
public class DataViewMappingData extends ValueObject {

    String alias;
    String dataViewCode;
    String dataViewType;
    String dataViewName;
    Map<String, Object> dataViewParams;
    String dataSourceCode;

    List<Map<String, Object>> requestDataStructure;
    List<Map<String, Object>> responseDataStructure;

}
