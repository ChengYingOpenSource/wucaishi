package com.cy.onepush.datapackager.application.params;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CreateDataPackagerParams {

    private String code;
    private String name;
    private String scriptType;
    private String scriptContent;

    private Map<String, String> dataViews;

    private List<Map<String, Object>> reqDataStructures;
    private List<Map<String, Object>> respDataStructures;

}
