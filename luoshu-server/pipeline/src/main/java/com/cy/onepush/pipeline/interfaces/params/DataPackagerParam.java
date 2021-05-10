package com.cy.onepush.pipeline.interfaces.params;

import com.cy.onepush.pipeline.interfaces.params.groups.PipelineCreateGatewayGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
public class DataPackagerParam {

    @NotBlank(groups = {PipelineCreateGatewayGroup.class},
        message = "the data packager code should not be blank")
    @Length(groups = {PipelineCreateGatewayGroup.class},
        min = 1, max = 64, message = "the code's length should between 1 and 64")
    private String dataPackagerCode;
    @Length(min = 1, max = 32, message = "the name's length should between 1 and 32")
    private String dataPackagerName;
    @NotBlank(message = "the script type should not be blank")
    @Length(max = 16, message = "the script type's length should between 0 and 16")
    private String scriptType;
    private String scriptContent;

    private List<@NotNull(message = "the request data structure item should not be null") Map<String, Object>> requestDataStructure;
    private List<@NotNull(message = "the response data structure item should not be null") Map<String, Object>> responseDataStructure;

}
