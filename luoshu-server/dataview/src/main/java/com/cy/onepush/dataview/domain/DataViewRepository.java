package com.cy.onepush.dataview.domain;

import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.dataview.interfaces.params.DebugDataViewParams;

import java.util.Collection;
import java.util.List;

/**
 * data view repository
 *
 * @author WhatAKitty
 * @version 0.1.0
 * @date 2020-11-15
 */
public interface DataViewRepository {

    DataView get(DataViewIdWithVersion dataViewIdWithVersion);

    DataView dataViewForDebug(DebugDataViewParams params);

    List<DataView> batchGet(Collection<DataViewIdWithVersion> dataViewIdWithVersions);

    Collection<DataView> all();

    void add(DataView dataView);

    void batchAdd(Collection<DataView> collection);

    void update(DataView dataView);

    void batchInsertOrUpdate(Collection<DataView> collection);

    void delete(DataView dataView);

    void batchDeleteByIds(Collection<DataViewIdWithVersion> dataViewIdWithVersions);

}
