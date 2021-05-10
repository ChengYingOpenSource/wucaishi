package com.cy.onepush.project.infrastructure.repository.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.cy.onepush.project.infrastructure.repository.mybatis.mapper")
public class ProjectMyBatisConfiguration {
}
