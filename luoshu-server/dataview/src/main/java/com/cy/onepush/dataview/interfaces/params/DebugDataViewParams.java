package com.cy.onepush.dataview.interfaces.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@Data
public class DebugDataViewParams {

    private String dataViewAlias;
    @NotBlank(message = "data view code should not be blank")
    private String dataViewCode;
    @NotEmpty(message = "data view params should not be blank")
    private Map<String, Object> dataViewParams;
    @NotBlank(message = "datasource code should not be blank")
    private String dataSourceCode;

    private List<Map<String, Object>> requestDataStructure;
    private List<Map<String, Object>> responseDataStructure;

    private DebugExecutionParams executionParams;

    @Data
    public static class DebugExecutionParams {

        private Map<String, Object> params;

    }

}
