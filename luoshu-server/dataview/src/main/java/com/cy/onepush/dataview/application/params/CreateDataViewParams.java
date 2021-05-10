package com.cy.onepush.dataview.application.params;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Data
public class CreateDataViewParams {

    private String code;
    private String name;
    private String dataSourceCode;

    private List<Map<String, Object>> reqDataStructures;
    private List<Map<String, Object>> respDataStructures;

}
