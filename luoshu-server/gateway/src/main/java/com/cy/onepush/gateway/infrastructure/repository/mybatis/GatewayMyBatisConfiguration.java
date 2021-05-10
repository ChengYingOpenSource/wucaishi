package com.cy.onepush.gateway.infrastructure.repository.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.cy.onepush.gateway.infrastructure.repository.mybatis.mapper")
public class GatewayMyBatisConfiguration {
}
