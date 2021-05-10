package com.cy.onepush.project.infrastructure.repository.mybatis.mapper;

import com.cy.onepush.project.infrastructure.repository.mybatis.MyBatisBaseDao;
import com.cy.onepush.project.infrastructure.repository.mybatis.bean.ProjectModuleDO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * ProjectModuleMapper继承基类
 */
public interface ProjectModuleMapper extends MyBatisBaseDao<ProjectModuleDO, Long> {

    ProjectModuleDO selectByCode(@Param("code") String code);

    List<ProjectModuleDO> selectByProjectCode(@Param("projectCode") String projectCode);

    List<ProjectModuleDO> selectByProjectCodes(Collection<String> collection);

    int batchInsertOrUpdate(Collection<ProjectModuleDO> collection);

    int batchDeleteByIds(Collection<Long> collection);

}