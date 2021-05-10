package com.cy.onepush.pipeline.infrastructure.repository.mybatis.assembly;

import com.cy.onepush.common.utils.SerializeUtils;
import com.cy.onepush.pipeline.domain.Pipeline;
import com.cy.onepush.pipeline.infrastructure.repository.mybatis.bean.PipelineDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Date;

@Mapper
public interface PipelineDOAssembly {

    PipelineDOAssembly ASSEMBLY = Mappers.getMapper(PipelineDOAssembly.class);

    default Pipeline domainFromDo(PipelineDO pipelineDO) {
        if (pipelineDO == null) {
            return null;
        }
        final byte[] bytes = pipelineDO.getData();
        return SerializeUtils.bytesToBean(bytes, Pipeline.class);
    }

    default PipelineDO doFromDomain(Pipeline pipeline, Date date) {
        final PipelineDO pipelineDO = new PipelineDO();
        pipelineDO.setCode(pipeline.getId().getId());
        if (pipeline.getGatewayId() != null) {
            pipelineDO.setDataInterfaceCode(pipeline.getGatewayId().getId());
        }
        pipelineDO.setStatus(pipeline.getPipelineStatus().getValue());
        pipelineDO.setData(SerializeUtils.beanToBytes(pipeline, Pipeline.class));
        pipelineDO.setVersion(pipeline.getGatewayData().getVersion());
        pipelineDO.setGmtCreated(date);
        pipelineDO.setGmtCreator(-1L);
        pipelineDO.setGmtModified(date);
        pipelineDO.setGmtModifier(-1L);
        return pipelineDO;
    }

}
