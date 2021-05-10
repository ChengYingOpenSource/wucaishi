package com.cy.onepush.datapackager.infrastructure.repository.mybatis.mapper;

import com.cy.onepush.datapackager.infrastructure.repository.mybatis.MyBatisBaseDao;
import com.cy.onepush.datapackager.infrastructure.repository.mybatis.bean.DataPackagerViewDO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * DataPackagerViewMapper继承基类
 */
public interface DataPackagerViewMapper extends MyBatisBaseDao<DataPackagerViewDO, Long> {

    List<DataPackagerViewDO> selectByDataPackagerCodeAndVersion(@Param("dataPackagerCode") String dataPackagerCode, @Param("version") String version);

    int batchInsert(Collection<DataPackagerViewDO> collection);

    int deleteByPackagerCodeAndVersion(@Param("dataPackagerCode") String dataPackagerCode, @Param("version") String version);

}