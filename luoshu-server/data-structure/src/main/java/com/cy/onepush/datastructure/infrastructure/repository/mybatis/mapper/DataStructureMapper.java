package com.cy.onepush.datastructure.infrastructure.repository.mybatis.mapper;

import com.cy.onepush.datastructure.infrastructure.repository.mybatis.MyBatisBaseDao;
import com.cy.onepush.datastructure.infrastructure.repository.mybatis.bean.DataStructureDO;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * DataStructureMapper继承基类
 */
public interface DataStructureMapper extends MyBatisBaseDao<DataStructureDO, Long> {

    List<DataStructureDO> selectByTargetCode(@Param("targetCode") String targetCode);

    int batchInsert(Collection<DataStructureDO> collection);

    int batchInsertOrUpdate(Collection<DataStructureDO> collection);

    void batchDeleteByTargetCode(Collection<String> collection);

}