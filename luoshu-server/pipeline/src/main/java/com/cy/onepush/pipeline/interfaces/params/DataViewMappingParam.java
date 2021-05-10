package com.cy.onepush.pipeline.interfaces.params;

import com.cy.onepush.pipeline.interfaces.params.groups.PipelineCreateGatewayGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
public class DataViewMappingParam {

    @Length(min = 1, max = 32, message = "the alias's length should between 1 and 32")
    private String alias;
    @NotBlank(groups = {PipelineCreateGatewayGroup.class},
        message = "the data view code should not be blank")
    @Length(groups = {PipelineCreateGatewayGroup.class},
        min = 1, max = 32, message = "the code's length should between 1 and 32")
    private String dataViewCode;
    private String dataViewType;
    @Length(min = 1, max = 32, message = "the name's length should between 1 and 32")
    private String dataViewName;
    @NotEmpty(message = "the data view params should not be empty")
    private Map<String, Object> dataViewParams;
    @NotBlank(message = "the data source code should not be null")
    @Length(min = 1, max = 32, message = "the data source code's length should between 1 and 32")
    private String dataSourceCode;

    private List<@NotNull(message = "the request data structure item should not be null") Map<String, Object>> requestDataStructure;
    private List<@NotNull(message = "the response data structure item should not be null") Map<String, Object>> responseDataStructure;

}
