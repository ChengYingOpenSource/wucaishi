package com.cy.onepush.dataview.application;

import com.cy.onepush.common.exception.Asserts;
import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.datasource.application.DataSourceApplicationService;
import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.dataview.domain.DataViewIdWithVersion;
import com.cy.onepush.dataview.domain.DataViewRepository;
import com.cy.onepush.dataview.infrastructure.plugin.DataViewPlugin;
import com.cy.onepush.dataview.interfaces.params.DebugDataViewParams;
import com.cy.onepush.debugtool.application.listener.DebugToolApplicationService;
import com.cy.onepush.debugtool.domain.DebugTool;
import com.cy.onepush.execution.context.domain.ExecutionContext;
import com.cy.onepush.plugins.domain.plugin.Plugin;
import com.cy.onepush.plugins.domain.plugin.PluginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * data view application service
 *
 * @author WhatAKitty
 * @date 2020-12-12
 * @since 0.1.0
 */
@Service
@RequiredArgsConstructor
public class DataViewApplicationService {

    private final DataViewRepository dataViewRepository;
    private final PluginRepository pluginRepository;

    private final DebugToolApplicationService debugToolApplicationService;
    private final DataSourceApplicationService dataSourceApplicationService;

    public DataView get(String dataViewId, String version) {
        final DataView dataView = dataViewRepository.get(DataViewIdWithVersion.of(DataViewId.of(dataViewId), Version.of(version)));
        Asserts.assertNotNull(dataView, dataViewId);

        return dataView;
    }

    public List<String> allViewTypes() {
        return dataSourceApplicationService.allTypes();
    }

//    @Transactional(rollbackFor = Throwable.class)
//    public void createDataView(CreateDataViewParams params) {
//        // get data source by id
//        final DataSource dataSource = dataSourceApplicationService.get(params.getDataSourceCode());
//
//        // create a data view
//        final DataView dataView = new DataView(DataViewId.of(params.getCode()), params.getName());
//        dataView.bindDataSource(dataSource);
//        dataView.bindRequestDataStructure(params.getReqDataStructures());
//        dataView.bindResponseDataStructure(params.getRespDataStructures());
//
//        // persist
//        dataViewRepository.add(dataView);
//    }

//    /**
//     * bind datasource with data view
//     *
//     * @param params the bind params
//     */
//    public void bindDataSource(BindDataSourceParams params) {
//        // get data source by id
//        final DataSource dataSource = dataSourceApplicationService.get(params.getDataSourceCode());
//        Asserts.assertNotNull(dataSource, params.getDataSourceCode());
//
//        // get data view by id
//        final DataView dataView = dataViewRepository.get(DataViewId.of(params.getDataViewCode()));
//        Asserts.assertNotNull(dataView, params.getDataViewCode());
//
//        // bind
//        dataView.bindDataSource(dataSource);
//
//        // update
//        dataViewRepository.update(dataView);
//    }

    /**
     * debug the data view
     *
     * @param params debug params
     */
    public String debug(DebugDataViewParams params) {
        final String uid = UUID.randomUUID().toString();

        // get the debug tool by data view code
        final DebugTool debugTool = debugToolApplicationService.getOrCreate(uid);
        debugTool.printLog("start to init debug context");

        // get temp
        params.setDataViewCode(uid);
        final DataView raw = dataViewRepository.dataViewForDebug(params);

        debugTool.printLog("debug context init successfully");

        try {
            // debug
            debugTool.printLog("start to debug data view");

            // dynamic create the spi
            final Optional<Plugin> dataViewPluginOptional = pluginRepository.getByClass(DataViewPlugin.class);
            dataViewPluginOptional.map(plugin -> (DataView) plugin.createSpiInstance(raw))
                .ifPresent(dataView -> dataView.debug(debugTool, params.getExecutionParams().getParams()));

            return uid;
        } finally {
            debugTool.printLog("the debug process finished");
        }
    }

    /**
     * execute the data view
     *
     * @param dataViewId       data view to be executed
     * @param version          data view version
     * @param executionContext the execution context along with the whole execution progress
     */
    public void execute(String dataViewId, String version, ExecutionContext executionContext) {
        // get data view by id
        final DataView dataView = dataViewRepository.get(DataViewIdWithVersion.of(DataViewId.of(dataViewId), Version.of(version)));
        Asserts.assertNotNull(dataView, dataViewId);

        // execute
        dataView.execute(executionContext);
    }

}
