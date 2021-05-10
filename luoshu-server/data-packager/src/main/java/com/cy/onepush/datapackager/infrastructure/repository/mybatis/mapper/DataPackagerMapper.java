package com.cy.onepush.datapackager.infrastructure.repository.mybatis.mapper;

import com.cy.onepush.datapackager.infrastructure.repository.mybatis.MyBatisBaseDao;
import com.cy.onepush.datapackager.infrastructure.repository.mybatis.bean.DataPackagerDO;
import org.apache.ibatis.annotations.Param;

/**
 * DataPackagerMapper继承基类
 */
public interface DataPackagerMapper extends MyBatisBaseDao<DataPackagerDO, Long> {

    DataPackagerDO selectByCodeAndVersion(@Param("code") String code, @Param("version") String version);

    int updateByCodeAndVersion(DataPackagerDO dataPackagerDO);

    int deleteByCodeAndVersion(@Param("code") String code, @Param("version") String version);

}