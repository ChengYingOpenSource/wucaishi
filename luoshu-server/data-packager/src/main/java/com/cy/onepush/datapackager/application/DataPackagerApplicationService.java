package com.cy.onepush.datapackager.application;

import com.cy.onepush.common.exception.Asserts;
import com.cy.onepush.common.publishlanguage.datapackager.DataPackagerId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.datapackager.domain.DataPackager;
import com.cy.onepush.datapackager.domain.DataPackagerIdWithVersion;
import com.cy.onepush.datapackager.domain.DataPackagerRepository;
import com.cy.onepush.datapackager.interfaces.params.DebugDataPackagerParams;
import com.cy.onepush.debugtool.application.listener.DebugToolApplicationService;
import com.cy.onepush.debugtool.domain.DebugTool;
import com.cy.onepush.execution.context.domain.ExecutionContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

/**
 * data packager service
 *
 * @author WhatAKitty
 * @date 2020-12-13
 * @since 0.1.0
 */
@Service
@RequiredArgsConstructor
public class DataPackagerApplicationService {

    private final DebugToolApplicationService debugToolApplicationService;

    private final DataPackagerRepository dataPackagerRepository;

    public DataPackager get(String dataPackagerId, String version) {
        final DataPackagerIdWithVersion dataPackagerIdWithVersion = DataPackagerIdWithVersion.of(DataPackagerId.of(dataPackagerId), Version.of(version));
        final DataPackager dataPackager = dataPackagerRepository.getDataPackager(dataPackagerIdWithVersion);
        Asserts.assertNotNull(dataPackager, dataPackagerId);

        return dataPackager;
    }
//
//    @Transactional(rollbackFor = Throwable.class)
//    public void create(CreateDataPackagerParams params) {
//        // create dataviews mapping
//        final Map<String, DataView> dataViewMap = dataViewApplicationService.batchGet(params.getDataViews().values()).stream()
//            .collect(Collectors.toMap(item -> item.getId().getId(), item -> item, (a, b) -> a));
//        final Map<String, DataView> dataViews = params.getDataViews().entrySet().stream()
//            .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), dataViewMap.get(entry.getValue())))
//            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//
//        // create data packager
//        final DataPackager dataPackager = new DataPackager(DataPackagerId.of(params.getCode()), params.getName(),
//            params.getScriptType(), params.getScriptContent());
//        dataPackager.bindRequestDataStructure(params.getReqDataStructures());
//        dataPackager.bindResponseDataStructure(params.getRespDataStructures());
//        dataViews.forEach(dataPackager::bindDataView);
//
//        dataPackagerRepository.add(dataPackager);
//    }

    public String debug(DebugDataPackagerParams params) {
        final String uid = UUID.randomUUID().toString();

        // get the debug tool by data packager code
        final DebugTool debugTool = debugToolApplicationService.getOrCreate(uid);
        debugTool.printLog("start to init debug context");

        // 创建临时组装服务
        params.getDataPackagerParam().setDataPackagerCode(uid);
        params.getDataPackagerParam().setDataPackagerAlias(uid);
        params.getDataViewMappingParams().forEach(item -> {
            item.setDataViewAlias(item.getDataViewCode());
            item.setDataViewCode(String.format("%s_%s", uid, item.getDataViewCode()));
        });
        final DataPackager dataPackager = dataPackagerRepository.getDataPackagerForDebug(params);

        debugTool.printLog("debug context init successfully");

        try {
            // debug
            debugTool.printLog("start to debug data packager");
            dataPackager.debug(debugTool, params.getExecutionParams().getParams());
            return uid;
        } finally {
            debugTool.printLog("finish debug data packager");
            dataPackagerRepository.destroyDataPackagerForDebug(dataPackager);
        }
    }

    public void execute(String dataPackagerId, String version, ExecutionContext executionContext) {
        // get the data packager
        final DataPackagerIdWithVersion dataPackagerIdWithVersion = DataPackagerIdWithVersion.of(DataPackagerId.of(dataPackagerId), Version.of(version));
        final DataPackager dataPackager = dataPackagerRepository.getDataPackager(dataPackagerIdWithVersion);
        Asserts.assertNotNull(dataPackager, dataPackagerId);

        // execute
        dataPackager.execute(executionContext);
    }

//    @Transactional(rollbackFor = Throwable.class)
//    public void bindDataView(String dataPackagerId, String alias, String dataViewId) {
//        // get the data view
//        final DataView dataView = dataViewApplicationService.get(dataViewId);
//
//        // get the data packager
//        final DataPackager dataPackager = dataPackagerRepository.getDataPackager(DataPackagerId.of(dataPackagerId));
//        Asserts.assertNotNull(dataPackager, dataPackagerId);
//
//        // bind data view
//        dataPackager.bindDataView(alias, dataView);
//
//        // persist
//        dataPackagerRepository.update(dataPackager);
//    }

    public Set<String> executableScriptTypes() {
        return dataPackagerRepository.executableScriptTypes();
    }

}
