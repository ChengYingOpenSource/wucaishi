package com.cy.onepush.pipeline.infrastructure.repository.mybatis.mapper;

import com.cy.onepush.pipeline.infrastructure.repository.mybatis.MyBatisBaseDao;
import com.cy.onepush.pipeline.infrastructure.repository.mybatis.bean.PipelineDO;
import org.apache.ibatis.annotations.Param;

/**
 * PipelineMapper继承基类
 */
public interface PipelineMapper extends MyBatisBaseDao<PipelineDO, Long> {

    PipelineDO selectByCode(@Param("code") String code);

    PipelineDO selectByGatewayCodeAndVersion(@Param("gatewayCode") String gatewayCode, @Param("gatewayVersion") String gatewayVersion);

    int updateByCodeAndVersion(PipelineDO pipelineDO);

    int updateByCodeAndVersionWithBLOBs(PipelineDO pipelineDO);

}