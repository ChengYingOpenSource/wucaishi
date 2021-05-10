package com.cy.onepush.project.interfaces.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectVO {

    private String code;
    private String name;
    private String description;
    private Date createTime;
    private Date updateTime;

}
