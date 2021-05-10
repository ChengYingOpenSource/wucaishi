package com.cy.onepush.project.infrastructure.repository.mybatis.mapper;

import com.cy.onepush.project.infrastructure.repository.mybatis.MyBatisBaseDao;
import com.cy.onepush.project.infrastructure.repository.mybatis.bean.ProjectDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ProjectMapper继承基类
 */
public interface ProjectMapper extends MyBatisBaseDao<ProjectDO, Long> {

    List<ProjectDO> selectAll(@Param("name") String name);

    ProjectDO selectByCode(@Param("code") String code);

    int updateByCode(ProjectDO projectDO);

    int deleteByCode(@Param("code") String code);

}