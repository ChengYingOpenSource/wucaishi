package com.cy.onepush.pipeline.domain;

import com.cy.onepush.common.framework.domain.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@EqualsAndHashCode(callSuper = true)
public class DataPackagerData extends ValueObject {

    String dataPackagerCode;
    String dataPackagerName;
    String scriptType;
    String scriptContent;

    List<Map<String, Object>> requestDataStructure;
    List<Map<String, Object>> responseDataStructure;

}
