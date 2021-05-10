package com.cy.onepush.datasource.infrastructure.repository.mybatis.mapper;

import com.cy.onepush.datasource.infrastructure.repository.mybatis.MyBatisBaseDao;
import com.cy.onepush.datasource.infrastructure.repository.mybatis.bean.DataSourceDO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * DataSourceMapper继承基类
 */
public interface DataSourceMapper extends MyBatisBaseDao<DataSourceDO, Long> {

    List<DataSourceDO> selectAll();

    DataSourceDO selectByCode(@Param("code") String code);

    void insertOrUpdate(DataSourceDO item);

    void batchAdd(Collection<DataSourceDO> collection);

    void batchInsertOrUpdate(Collection<DataSourceDO> collection);

    void deleteByCode(@Param("code") String code);

}