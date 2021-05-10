package com.cy.onepush.datasource.interfaces.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Data
public class ValidateDataSourceParams {

    @NotBlank(message = "datasource type should not be blank")
    private String dataSourceType;
    @NotEmpty(message = "datasource properties should not be empty")
    private Map<String, String> dataSourceProperties;

}
