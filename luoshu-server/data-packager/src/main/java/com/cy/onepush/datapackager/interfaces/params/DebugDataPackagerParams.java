package com.cy.onepush.datapackager.interfaces.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
public class DebugDataPackagerParams {

    @NotNull(message = "the data packager param should not be null")
    private DataPackagerParam dataPackagerParam;
    @NotEmpty(message = "the data packager param should not be null")
    private List<DataViewMappingParam> dataViewMappingParams;
    @NotNull(message = "the execution params should not be null")
    private DebugExecutionParams executionParams;
    @NotBlank(message = "the version should not be null")
    private String version;

    @Data
    public static class DataPackagerParam {

        private String dataPackagerAlias;
        @NotBlank(message = "data packager code should not be blank")
        private String dataPackagerCode;
        @NotBlank(message = "script type should not be blank")
        private String scriptType;
        @NotBlank(message = "script content should not be blank")
        private String scriptContent;

        private List<Map<String, Object>> requestDataStructure;
        private List<Map<String, Object>> responseDataStructure;

    }

    @Data
    public static class DataViewMappingParam {

        private String dataViewAlias;
        @NotBlank(message = "data view code should not be blank")
        private String dataViewCode;
        @NotBlank(message = "data source code should not be blank")
        private String dataSourceCode;
        @NotEmpty(message = "data view params should not be empty")
        private Map<String, Object> dataViewParams;

        private List<Map<String, Object>> requestDataStructure;
        private List<Map<String, Object>> responseDataStructure;

    }

    @Data
    public static class DebugExecutionParams {

        @NotEmpty(message = "the params in debug execution params should not be empty")
        private Map<String, Object> params;

    }

}
