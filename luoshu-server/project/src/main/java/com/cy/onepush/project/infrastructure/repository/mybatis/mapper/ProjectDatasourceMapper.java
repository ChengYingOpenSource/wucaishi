package com.cy.onepush.project.infrastructure.repository.mybatis.mapper;

import com.cy.onepush.project.infrastructure.repository.mybatis.MyBatisBaseDao;
import com.cy.onepush.project.infrastructure.repository.mybatis.bean.ProjectDatasourceDO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * ProjectDatasourceMapper继承基类
 */
public interface ProjectDatasourceMapper extends MyBatisBaseDao<ProjectDatasourceDO, Long> {

    List<ProjectDatasourceDO> selectByProjectCode(@Param("projectCode") String projectCode);

    List<ProjectDatasourceDO> selectByProjectCodes(Collection<String> collection);

    int batchInsertIgnore(Collection<ProjectDatasourceDO> collection);

    int batchDeleteByIds(Collection<Long> collection);

}