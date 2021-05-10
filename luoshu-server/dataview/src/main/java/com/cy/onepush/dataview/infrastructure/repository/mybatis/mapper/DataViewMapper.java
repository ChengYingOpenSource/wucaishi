package com.cy.onepush.dataview.infrastructure.repository.mybatis.mapper;

import com.cy.onepush.dataview.infrastructure.repository.mybatis.MyBatisBaseDao;
import com.cy.onepush.dataview.infrastructure.repository.mybatis.bean.DataViewDO;
import com.cy.onepush.dataview.infrastructure.repository.mybatis.params.CodeAndVersionsParam;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * DataViewMapper继承基类
 */
public interface DataViewMapper extends MyBatisBaseDao<DataViewDO, Long> {

    List<DataViewDO> selectAll();

    int batchInsert(Collection<DataViewDO> collection);

    int updateByCodeAndVersion(DataViewDO dataViewDO);

    int batchInsertOrUpdate(Collection<DataViewDO> collection);

    void deleteByCodeAndVersion(@Param("code") String code, @Param("version") String version);

    void batchDeleteByCodeAndVersions(Collection<CodeAndVersionsParam> collection);

}