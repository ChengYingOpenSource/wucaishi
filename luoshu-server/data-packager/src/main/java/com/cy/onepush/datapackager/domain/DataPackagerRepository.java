package com.cy.onepush.datapackager.domain;

import com.cy.onepush.common.publishlanguage.datapackager.DataPackagerId;
import com.cy.onepush.datapackager.interfaces.params.DebugDataPackagerParams;

import java.util.Set;

/**
 * data packager repository
 *
 * @author WhatAKitty
 * @version 0.1.0
 * @date 2020-11-24
 */
public interface DataPackagerRepository {

    /**
     * the executable script types such as groovy, ecmascript, python and so on...
     *
     * @return the list of executable script types
     */
    Set<String> executableScriptTypes();

    /**
     * get the data packager by the data packager id
     *
     * @param dataPackagerIdWithVersion the data pcakger id
     * @return the data packager
     */
    DataPackager getDataPackager(DataPackagerIdWithVersion dataPackagerIdWithVersion);

    DataPackager getDataPackagerForDebug(DebugDataPackagerParams params);

    /**
     * persist the data packager into the repository
     *
     * @param dataPackager the data packager to be persisted
     */
    void add(DataPackager dataPackager);

    /**
     * update the data packager by packager code
     *
     * @param dataPackager the data to be updated
     */
    void update(DataPackager dataPackager);

    void destroyDataPackagerForDebug(DataPackager dataPackager);

    void delete(DataPackager dataPackager);

}
