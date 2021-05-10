package com.cy.onepush.pipeline.application;

import com.cy.onepush.common.exception.Asserts;
import com.cy.onepush.common.publishlanguage.gateway.GatewayId;
import com.cy.onepush.common.publishlanguage.pipeline.PipelineId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.datapackager.domain.DataPackager;
import com.cy.onepush.datapackager.domain.DataPackagerRepository;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.datasource.domain.DataSourceRepository;
import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.dataview.domain.DataViewRepository;
import com.cy.onepush.dataview.infrastructure.plugin.DataViewPlugin;
import com.cy.onepush.gateway.domain.Gateway;
import com.cy.onepush.gateway.domain.GatewayIdWithVersion;
import com.cy.onepush.gateway.domain.GatewayRepository;
import com.cy.onepush.pipeline.domain.Pipeline;
import com.cy.onepush.pipeline.domain.PipelineRepository;
import com.cy.onepush.pipeline.interfaces.params.DataPackagerParam;
import com.cy.onepush.pipeline.interfaces.params.DataViewMappingParam;
import com.cy.onepush.pipeline.interfaces.params.GatewayParam;
import com.cy.onepush.pipeline.interfaces.params.PipelineCreateGatewayParam;
import com.cy.onepush.plugins.domain.plugin.Plugin;
import com.cy.onepush.plugins.domain.plugin.PluginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PipelineApplicationService {

    private final PipelineRepository pipelineRepository;
    private final GatewayRepository gatewayRepository;
    private final DataPackagerRepository dataPackagerRepository;
    private final DataViewRepository dataViewRepository;
    private final PluginRepository pluginRepository;
    private final DataSourceRepository dataSourceRepository;

    public Pipeline get(String pipelineId) {
        final Optional<Pipeline> pipelineOptional = pipelineRepository.get(PipelineId.of(pipelineId));

        return pipelineOptional.orElseThrow(() -> Asserts.notFoundFail(pipelineId));
    }

    public Pipeline getByGatewayCodeAndVersion(String gatewayId, String version) {
        final GatewayIdWithVersion gatewayIdWithVersion = GatewayIdWithVersion.of(GatewayId.of(gatewayId), Version.of(version));
        final Optional<Pipeline> pipelineOptional = pipelineRepository.getByGatewayIdWithVersion(gatewayIdWithVersion);

        return pipelineOptional.orElseThrow(() -> Asserts.notFoundFail(gatewayId));
    }

    @Transactional(rollbackFor = Throwable.class)
    public Gateway pipelineCreate(String dataApiContextUrl, PipelineCreateGatewayParam param) {
        final Map<String, DataSource> dsMap = dataSourceRepository.allDataSources().stream()
            .collect(Collectors.toMap(item -> item.getId().getId(), item -> item, (a, b) -> a));

        final Pipeline pipeline = new Pipeline(PipelineId.of(UUID.randomUUID().toString()));
        param.getDataViewMappingParams().forEach(item -> pipeline.addDataView(item.getAlias(), item, dsMap));
        pipeline.addDataPackager(param.getDataPackagerParam());
        pipeline.addGateway(dataApiContextUrl, param.getGatewayParam(), param.getVersion());

        // add gateway
        final Gateway gateway = pipeline.createGateway(dataSourceRepository.allDataSources());
        gatewayRepository.add(gateway);

        // insert pipeline
        pipelineRepository.add(pipeline);

        // refresh data views plugin
        pluginRepository.getByClass(DataViewPlugin.class).ifPresent(Plugin::refresh);

        return gateway;
    }

    @Transactional(rollbackFor = Throwable.class)
    public Gateway pipelineUpdate(String dataApiContextUrl, String gatewayCode, PipelineCreateGatewayParam param, String version) {
        final GatewayIdWithVersion gatewayIdWithVersion = GatewayIdWithVersion.of(GatewayId.of(gatewayCode), Version.of(version));
        final Optional<Pipeline> pipelineOptional = pipelineRepository.getByGatewayIdWithVersion(gatewayIdWithVersion);
        if (!pipelineOptional.isPresent()) {
            return null;
        }

        final Pipeline pipeline = pipelineOptional.get();
        pipeline.resetStatus();

        final Gateway oldGateway = gatewayRepository.get(gatewayIdWithVersion);
        Asserts.assertNotNull(oldGateway, gatewayCode);

        // set value from old gateway domain value
        param.setVersion(oldGateway.getGatewayVersion().getId());
        param.getGatewayParam().setGatewayCode(oldGateway.getId().getId());
        param.getDataPackagerParam().setDataPackagerCode(oldGateway.getDataPackager().getId().getId());

        final Map<String, DataSource> dsMap = dataSourceRepository.allDataSources().stream()
            .collect(Collectors.toMap(item -> item.getId().getId(), item -> item, (a, b) -> a));

        // create new pipeline
        final Pipeline newPipeline = new Pipeline(pipeline.getId());
        param.getDataViewMappingParams().forEach(item -> newPipeline.addDataView(item.getAlias(), item, dsMap));
        newPipeline.addDataPackager(param.getDataPackagerParam());
        newPipeline.addGateway(dataApiContextUrl, param.getGatewayParam(), version);

        // add gateway
        final Gateway gateway = newPipeline.createGateway(dataSourceRepository.allDataSources());
        gatewayRepository.update(gateway);

        // insert pipeline
        pipelineRepository.update(newPipeline);

        // refresh data views plugin
        pluginRepository.getByClass(DataViewPlugin.class).ifPresent(Plugin::refresh);

        return gateway;
    }

    @Transactional(rollbackFor = Throwable.class)
    public Pipeline startPipeline() {
        final Pipeline pipeline = new Pipeline(PipelineId.of(UUID.randomUUID().toString()));

        pipelineRepository.add(pipeline);

        return pipeline;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void addDataView(String pipelineId, DataViewMappingParam dataViewMappingParam) {
        final Map<String, DataSource> dsMap = dataSourceRepository.allDataSources().stream()
            .collect(Collectors.toMap(item -> item.getId().getId(), item -> item, (a, b) -> a));

        final Optional<Pipeline> pipelineOptional = pipelineRepository.get(PipelineId.of(pipelineId));
        final Pipeline pipeline = pipelineOptional.orElseThrow(() -> Asserts.notFoundFail(pipelineId));

        pipeline.addDataView(dataViewMappingParam.getAlias(), dataViewMappingParam, dsMap);

        pipelineRepository.update(pipeline);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void addDataPackager(String pipelineId, DataPackagerParam dataPackagerParam) {
        final Optional<Pipeline> pipelineOptional = pipelineRepository.get(PipelineId.of(pipelineId));
        final Pipeline pipeline = pipelineOptional.orElseThrow(() -> Asserts.notFoundFail(pipelineId));

        pipeline.addDataPackager(dataPackagerParam);

        pipelineRepository.update(pipeline);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void addGateway(String pipelineId, GatewayParam gatewayParam) {
        final Optional<Pipeline> pipelineOptional = pipelineRepository.get(PipelineId.of(pipelineId));
        final Pipeline pipeline = pipelineOptional.orElseThrow(() -> Asserts.notFoundFail(pipelineId));

        // TODO calculate context path wrong, need to fix it.
        pipeline.addGateway(pipeline.getGatewayData().getGatewayUri(), gatewayParam, null);

        pipelineRepository.update(pipeline);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void create(String pipelineId) {
        // update pipeline info
        final Optional<Pipeline> pipelineOptional = pipelineRepository.get(PipelineId.of(pipelineId));
        final Pipeline pipeline = pipelineOptional.orElseThrow(() -> Asserts.notFoundFail(pipelineId));

        pipelineRepository.update(pipeline);

        // add gateway
        final Gateway gateway = pipeline.createGateway(dataSourceRepository.allDataSources());
        gatewayRepository.add(gateway);

        // add data packager
        final DataPackager dataPackager = gateway.getDataPackager();
        dataPackagerRepository.add(dataPackager);

        // add data views
        final Map<String, DataView> dataViews = dataPackager.getDataViews();
        dataViewRepository.batchAdd(dataViews.values());

        // refresh data views plugin
        pluginRepository.getByClass(DataViewPlugin.class).ifPresent(Plugin::refresh);

    }

}
