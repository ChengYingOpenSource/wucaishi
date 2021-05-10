package com.cy.onepush.project.interfaces.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectModuleVO {

    private String name;
    private String moduleCode;
    private String projectCode;
    private String description;
    private Date createTime;
    private Date updateTime;

}
