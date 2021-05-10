package com.cy.onepush.project.interfaces.params;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateProjectParams {

    @NotBlank(message = "the name should not be blank")
    @Length(min = 1, max = 32, message = "the name's length should between 1 and 32")
    private String name;
    @Length(max = 512, message = "the description's length should between 0 and 512")
    private String description;

}
