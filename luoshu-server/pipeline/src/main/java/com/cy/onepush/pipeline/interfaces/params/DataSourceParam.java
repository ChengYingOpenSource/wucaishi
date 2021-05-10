package com.cy.onepush.pipeline.interfaces.params;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class DataSourceParam {

    @NotBlank(message = "the data source code should not be blank")
    @Length(min = 1, max = 64, message = "the source code's length should between 1 and 64")
    String dataSourceCode;
    @NotBlank(message = "the data source name should not be blank")
    @Length(min = 1, max = 32, message = "the source name's length should between 1 and 32")
    String dataSourceName;
    @NotBlank(message = "the data view name should not be blank")
    @Length(min = 1, max = 32, message = "the source type's length should between 1 and 32")
    String dataSourceType;
    Map<String, String> dataSourceProperties;

}
