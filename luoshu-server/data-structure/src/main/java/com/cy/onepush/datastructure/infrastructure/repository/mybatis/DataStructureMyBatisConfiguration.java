package com.cy.onepush.datastructure.infrastructure.repository.mybatis;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@MapperScan(basePackages = "com.cy.onepush.datastructure.infrastructure.repository.mybatis.mapper")
public class DataStructureMyBatisConfiguration {

}
