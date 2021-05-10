package com.cy.onepush.pipeline.interfaces.params;

import com.cy.onepush.pipeline.interfaces.params.groups.PipelineCreateGatewayGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
public class GatewayParam {

    @NotBlank(groups = {PipelineCreateGatewayGroup.class},
        message = "the code should not be blank")
    @Length(groups = {PipelineCreateGatewayGroup.class},
        min = 1, max = 64, message = "the code's length should between 1 and 64")
    private String gatewayCode;
    @Length(min = 1, max = 32, message = "the name's length should between 1 and 32")
    private String gatewayName;
    @Length(max = 8, message = "the method's length should between 0 and 8")
    private String gatewayMethod;
    @Length(max = 1024, message = "the uri's length should between 0 and 1024")
    private String gatewayUri;
    @Length(max = 1024, message = "the description's length should between 0 and 1024")
    private String gatewayDescription;

}
