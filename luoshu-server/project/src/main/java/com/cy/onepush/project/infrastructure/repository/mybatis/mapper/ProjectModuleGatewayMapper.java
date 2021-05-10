package com.cy.onepush.project.infrastructure.repository.mybatis.mapper;

import com.cy.onepush.project.infrastructure.repository.mybatis.MyBatisBaseDao;
import com.cy.onepush.project.infrastructure.repository.mybatis.bean.ProjectModuleGatewayDO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * ProjectModuleGatewayMapper继承基类
 */
public interface ProjectModuleGatewayMapper extends MyBatisBaseDao<ProjectModuleGatewayDO, Long> {

    List<ProjectModuleGatewayDO> selectByProjectCode(@Param("projectCode") String projectCode);

    List<ProjectModuleGatewayDO> selectByProjectCodes(Collection<String> collection);

    List<ProjectModuleGatewayDO> selectByModuleCode(@Param("moduleCode") String moduleCode);

    int batchInsert(Collection<ProjectModuleGatewayDO> collection);

    int batchDelete(Collection<Long> collection);

}