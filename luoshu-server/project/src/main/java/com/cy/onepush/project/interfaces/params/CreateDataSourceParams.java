package com.cy.onepush.project.interfaces.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class CreateDataSourceParams {

    @NotBlank(message = "datasource code should not be blank")
    private String dataSourceCode;
    @NotBlank(message = "datasource name should not be blank")
    private String dataSourceName;
    @NotBlank(message = "datasource type should not be blank")
    private String dataSourceType;
    private Map<String, String> dataSourceProperties;

}
