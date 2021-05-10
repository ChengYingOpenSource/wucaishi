package com.cy.onepush.gateway.infrastructure.repository.mybatis.mapper;

import com.cy.onepush.gateway.infrastructure.repository.mybatis.MyBatisBaseDao;
import com.cy.onepush.gateway.infrastructure.repository.mybatis.bean.DataInterfaceDO;
import com.cy.onepush.gateway.infrastructure.repository.mybatis.params.SearchDataInterfacesDOParams;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * DataInterfaceMapper继承基类
 */
public interface DataInterfaceMapper extends MyBatisBaseDao<DataInterfaceDO, Long> {

    DataInterfaceDO selectByCodeAndVersion(@Param("code") String code, @Param("version") String version);

    List<DataInterfaceDO> selectAll();

    List<DataInterfaceDO> selectByParams(SearchDataInterfacesDOParams params);

    void insertOrUpdate(DataInterfaceDO record);

    void deleteByCodeAndVersion(@Param("code") String code, @Param("version") String version);

}