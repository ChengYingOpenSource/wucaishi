package com.cy.onepush.datapackager.infrastructure.repository.mybatis.mapper;

import com.cy.onepush.datapackager.infrastructure.repository.mybatis.MyBatisBaseDao;
import com.cy.onepush.datapackager.infrastructure.repository.mybatis.bean.DataPackagerScriptDO;
import org.apache.ibatis.annotations.Param;

/**
 * DataPackagerScriptMapper继承基类
 */
public interface DataPackagerScriptMapper extends MyBatisBaseDao<DataPackagerScriptDO, Long> {

    DataPackagerScriptDO selectByDataPackagerCodeAndVersion(@Param("dataPackagerCode") String dataPackagerCode, @Param("version") String version);

    int updateByDataPackagerCodeWithVersion(DataPackagerScriptDO dataPackagerScriptDO);

    int updateByDataPackagerCodeWithVersionAndBLOBs(DataPackagerScriptDO dataPackagerScriptDO);

    int deleteByDataPackagerCodeAndVersion(@Param("dataPackagerCode") String dataPackagerCode, @Param("version") String version);

}