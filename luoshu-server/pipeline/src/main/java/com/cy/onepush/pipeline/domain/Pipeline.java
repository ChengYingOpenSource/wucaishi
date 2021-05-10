package com.cy.onepush.pipeline.domain;

import com.cy.onepush.common.exception.Asserts;
import com.cy.onepush.common.exception.ResourceNotFoundException;
import com.cy.onepush.common.framework.domain.AbstractAggregateRoot;
import com.cy.onepush.common.publishlanguage.datapackager.DataPackagerId;
import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.common.publishlanguage.gateway.GatewayId;
import com.cy.onepush.common.publishlanguage.pipeline.PipelineId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.common.utils.URLAppender;
import com.cy.onepush.datapackager.domain.DataPackager;
import com.cy.onepush.datapackager.infrastructure.holder.ScriptExecutionEngineHolder;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.gateway.domain.Gateway;
import com.cy.onepush.pipeline.domain.assembly.DataPackagerDataAssembly;
import com.cy.onepush.pipeline.domain.assembly.DataViewMappingDataAssembly;
import com.cy.onepush.pipeline.domain.assembly.GatewayDataAssembly;
import com.cy.onepush.pipeline.domain.event.PipelineDoneEvent;
import com.cy.onepush.pipeline.interfaces.params.DataPackagerParam;
import com.cy.onepush.pipeline.interfaces.params.DataViewMappingParam;
import com.cy.onepush.pipeline.interfaces.params.GatewayParam;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * pipeline for create gateway
 *
 * @author WhatAKitty
 * @date 2020-12-16
 * @since 0.1.0
 */
@Slf4j
public class Pipeline extends AbstractAggregateRoot<String> {

    private PipelineStatus pipelineStatus;
    private GatewayData gatewayData;
    private DataPackagerData dataPackagerData;
    private final Map<String, DataViewMappingData> dataViewMappingDataMap;

    public Pipeline(PipelineId id) {
        super(id);
        this.pipelineStatus = PipelineStatus.STARTED;
        this.dataViewMappingDataMap = new HashMap<>(8);
    }

    @Override
    public PipelineId getId() {
        return PipelineId.of(super.getId().getId());
    }

    public GatewayId getGatewayId() {
        if (gatewayData == null) {
            return null;
        }
        return GatewayId.of(gatewayData.getGatewayCode());
    }

    public PipelineStatus getPipelineStatus() {
        return this.pipelineStatus;
    }

    public void resetStatus() {
        this.pipelineStatus = PipelineStatus.STARTED;
    }

    public void addDataView(String alias, DataViewMappingParam dataViewMappingParam, Map<String, DataSource> dsMap) {
        Asserts.assertState(isDone(), false);

        final String newAlias = StringUtils.isBlank(alias) ? dataViewMappingParam.getDataViewCode() : alias;
        dataViewMappingParam.setAlias(newAlias);

        final DataSource dataSource = dsMap.get(dataViewMappingParam.getDataSourceCode());
        if (dataSource == null) {
            throw new ResourceNotFoundException("the datasource coded %s not found", dataViewMappingParam.getDataSourceCode());
        }
        dataViewMappingParam.setDataViewType(dataSource.getType());

        final DataViewMappingData dataViewMappingData = DataViewMappingDataAssembly.ASSEMBLY.dataFromParam(dataViewMappingParam);
        this.dataViewMappingDataMap.put(newAlias, dataViewMappingData);
    }

    public void addDataViews(Collection<DataViewMappingParam> collection) {
        Asserts.assertState(isDone(), false);

        final Map<String, DataViewMappingData> dataViewMappingData = DataViewMappingDataAssembly.ASSEMBLY.dataFromParams(collection).stream()
            .collect(Collectors.toMap(
                item -> (StringUtils.isBlank(item.getAlias()) ? item.getDataViewCode() : item.getAlias()),
                item -> item,
                (a, b) -> a
            ));

        this.dataViewMappingDataMap.putAll(dataViewMappingData);
    }

    public void updateDataViews(Collection<DataViewMappingParam> collection) {
        Asserts.assertState(isDone(), false);

        final Map<String, DataViewMappingData> dataViewMappingData = DataViewMappingDataAssembly.ASSEMBLY.dataFromParams(collection).stream()
            .collect(Collectors.toMap(
                item -> (StringUtils.isBlank(item.getAlias()) ? item.getDataViewCode() : item.getAlias()),
                item -> item,
                (a, b) -> a
            ));

        this.dataViewMappingDataMap.clear();
        this.dataViewMappingDataMap.putAll(dataViewMappingData);
    }

    public void addDataPackager(DataPackagerParam dataPackagerParam) {
        Asserts.assertState(isDone(), false);

        this.dataPackagerData = DataPackagerDataAssembly.ASSEMBLY.dataFromParam(dataPackagerParam);
    }

    public void addGateway(String contextPath, GatewayParam gatewayParam, String version) {
        Asserts.assertState(isDone(), false);

        if (StringUtils.isNotBlank(contextPath)) {
            final String uri = URLAppender.getInstance()
                .append(contextPath)
                .append(StringUtils.isBlank(gatewayParam.getGatewayUri()) ? gatewayParam.getGatewayCode() : gatewayParam.getGatewayUri())
                .append(version)
                .build();
            gatewayParam.setGatewayUri(uri);
        }

        this.gatewayData = GatewayDataAssembly.ASSEMBLY.dataFromParam(gatewayParam, version);
    }

    public Gateway createGateway(Collection<DataSource> dataSources) {
        Asserts.assertState(isDone(), false);

        final Version version = Version.of(gatewayData.getVersion());

        final Map<String, DataSource> dataSourceMap = dataSources.stream()
            .collect(Collectors.toMap(item -> item.getId().getId(), item -> item, (a, b) -> a));

        try {
            // create data view mapping
            final Map<String, DataView> dataViewMap = dataViewMappingDataMap.entrySet().stream()
                .map(entry -> {
                    final String alias = entry.getKey();
                    final DataViewMappingData dataViewMappingData = entry.getValue();
                    final String dataSourceCode = dataViewMappingData.getDataSourceCode();

                    DataView dataView = new DataView(DataViewId.of(dataViewMappingData.getDataViewCode()), dataViewMappingData.getDataViewName(), version);
                    dataView.bindDataSource(dataSourceMap.get(dataSourceCode));
                    dataView.bindRequestDataStructure(dataViewMappingData.getRequestDataStructure());
                    dataView.bindResponseDataStructure(dataViewMappingData.getResponseDataStructure());
                    dataView.setParams(dataViewMappingData.getDataViewParams());

                    return new AbstractMap.SimpleEntry<>(alias, dataView);
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            // create data packager
            final DataPackager dataPackager = new DataPackager(DataPackagerId.of(dataPackagerData.getDataPackagerCode()),
                dataPackagerData.getDataPackagerName(), dataPackagerData.getScriptType(), dataPackagerData.getScriptContent(),
                version);
            dataPackager.bindRequestDataStructure(dataPackagerData.getRequestDataStructure());
            dataPackager.bindResponseDataStructure(dataPackagerData.getResponseDataStructure());
            dataViewMap.forEach(dataPackager::bindDataView);
            dataPackager.bindScriptExecutionEngine(ScriptExecutionEngineHolder.INSTANCE.getByType(dataPackagerData.getScriptType()));

            // create gateway
            final Gateway gateway = new Gateway(GatewayId.of(gatewayData.getGatewayCode()),
                gatewayData.getGatewayName(), gatewayData.getGatewayMethod(), gatewayData.getGatewayUri(),
                version);
            gateway.setDescription(gatewayData.getGatewayDescription());
            gateway.bindDataPackager(dataPackager);

            // set status to finished
            this.pipelineStatus = PipelineStatus.FINISHED;

            return gateway;
        } catch (Error error) {
            // catch
            this.pipelineStatus = PipelineStatus.FAILED;

            log.error("failed to create gateway due to ", error);

            throw error;
        } finally {
            // publish done event
            publishEvent(new PipelineDoneEvent(getId(), this.pipelineStatus));
        }
    }

    public GatewayData getGatewayData() {
        return gatewayData;
    }

    public DataPackagerData getDataPackagerData() {
        return dataPackagerData;
    }

    public Map<String, DataViewMappingData> getDataViewMappingDataMap() {
        return ImmutableMap.copyOf(dataViewMappingDataMap);
    }

    public boolean isDone() {
        return PipelineStatus.FINISHED.equals(this.pipelineStatus)
            || PipelineStatus.FAILED.equals(this.pipelineStatus);
    }

}
