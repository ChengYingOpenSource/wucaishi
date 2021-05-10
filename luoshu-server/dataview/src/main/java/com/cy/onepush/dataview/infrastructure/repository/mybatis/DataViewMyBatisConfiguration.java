package com.cy.onepush.dataview.infrastructure.repository.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.cy.onepush.dataview.infrastructure.repository.mybatis.mapper")
public class DataViewMyBatisConfiguration {
}
