package com.cy.onepush.datapackager.infrastructure.repository;

import com.cy.onepush.common.exception.Asserts;
import com.cy.onepush.common.exception.DuplicateResourceException;
import com.cy.onepush.common.exception.ResourceNotFoundException;
import com.cy.onepush.common.publishlanguage.datapackager.DataPackagerId;
import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.datapackager.domain.DataPackager;
import com.cy.onepush.datapackager.domain.DataPackagerIdWithVersion;
import com.cy.onepush.datapackager.domain.DataPackagerRepository;
import com.cy.onepush.datapackager.infrastructure.holder.ScriptExecutionEngineHolder;
import com.cy.onepush.datapackager.infrastructure.repository.mybatis.assembly.DataPackagerAssembly;
import com.cy.onepush.datapackager.infrastructure.repository.mybatis.bean.DataPackagerDO;
import com.cy.onepush.datapackager.infrastructure.repository.mybatis.bean.DataPackagerScriptDO;
import com.cy.onepush.datapackager.infrastructure.repository.mybatis.bean.DataPackagerViewDO;
import com.cy.onepush.datapackager.infrastructure.repository.mybatis.mapper.DataPackagerMapper;
import com.cy.onepush.datapackager.infrastructure.repository.mybatis.mapper.DataPackagerScriptMapper;
import com.cy.onepush.datapackager.infrastructure.repository.mybatis.mapper.DataPackagerViewMapper;
import com.cy.onepush.datapackager.interfaces.params.DebugDataPackagerParams;
import com.cy.onepush.datastructure.domain.DataStructure;
import com.cy.onepush.datastructure.domain.DataStructureRepository;
import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.dataview.domain.DataViewIdWithVersion;
import com.cy.onepush.dataview.domain.DataViewRepository;
import com.cy.onepush.dataview.infrastructure.plugin.DataViewPlugin;
import com.cy.onepush.dataview.interfaces.params.DebugDataViewParams;
import com.cy.onepush.plugins.domain.plugin.Plugin;
import com.cy.onepush.plugins.domain.plugin.PluginRepository;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RdsDataPackagerRepository implements DataPackagerRepository {

    private final DataPackagerMapper dataPackagerMapper;
    private final DataPackagerViewMapper dataPackagerViewMapper;
    private final DataPackagerScriptMapper dataPackagerScriptMapper;

    private final DataStructureRepository dataStructureRepository;
    private final DataViewRepository dataViewRepository;
    private final PluginRepository pluginRepository;

    @Override
    public Set<String> executableScriptTypes() {
        return ScriptExecutionEngineHolder.INSTANCE.allTypes();
    }

    @Override
    public DataPackager getDataPackager(DataPackagerIdWithVersion dataPackagerIdWithVersion) {
        final String dataPackagerCode = dataPackagerIdWithVersion.getDataPackagerId().getId();
        final String version = dataPackagerIdWithVersion.getVersion().getId();
        // get data packager
        final DataPackagerDO dataPackagerDO = dataPackagerMapper.selectByCodeAndVersion(dataPackagerCode, version);
        Asserts.assertNotNull(dataPackagerDO, dataPackagerCode);

        // get data packager script
        final DataPackagerScriptDO dataPackagerScriptDO = dataPackagerScriptMapper.selectByDataPackagerCodeAndVersion(dataPackagerCode, version);

        // get data views
        final List<DataPackagerViewDO> dataPackagerViewDOS = dataPackagerViewMapper.selectByDataPackagerCodeAndVersion(dataPackagerCode, version);
        final Set<DataViewIdWithVersion> dataViewIdWithVersions = dataPackagerViewDOS.stream()
            .map(item -> DataViewIdWithVersion.of(DataViewId.of(item.getDataViewCode()), Version.of(version)))
            .collect(Collectors.toSet());
        final Map<String, DataView> dataViewMap = dataViewRepository.batchGet(dataViewIdWithVersions).stream()
            .collect(Collectors.toMap(item -> item.getId().getId(), item -> item, (a, b) -> a));
        final Map<String, DataView> dataViews = dataPackagerViewDOS.stream()
            .filter(item -> dataViewMap.get(item.getDataViewCode()) != null)
            .map(dataPackagerViewDo -> {
                final DataView dataView = dataViewMap.get(dataPackagerViewDo.getDataViewCode());
                return new AbstractMap.SimpleEntry<>(dataPackagerViewDo.getDataViewAlias(), dataView);
            })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // create data packager
        final DataPackager dataPackager = new DataPackager(DataPackagerId.of(dataPackagerDO.getCode()), dataPackagerDO.getName(),
            dataPackagerScriptDO.getScriptType(), dataPackagerScriptDO.getScriptContent(), Version.of(dataPackagerDO.getVersion())
        );
        dataPackager.bindScriptExecutionEngine(ScriptExecutionEngineHolder.INSTANCE.getByType(dataPackagerScriptDO.getScriptType()));
        dataViews.forEach(dataPackager::bindDataView);

        // get request and response data structure
        final Optional<List<DataStructure>> reqStructureOptional = dataStructureRepository.getByTargetCode(dataPackager.getRequestDataStructure().getId().getId() + "_" + dataPackager.getVersion().getId());
        final Optional<List<DataStructure>> respStructureOptional = dataStructureRepository.getByTargetCode(dataPackager.getResponseDataStructure().getId().getId() + "_" + dataPackager.getVersion().getId());
        dataPackager.addAllRequestDataStructure(reqStructureOptional.orElse(Lists.newArrayListWithCapacity(0)));
        dataPackager.addAllResponseDataStructure(respStructureOptional.orElse(Lists.newArrayListWithCapacity(0)));

        return dataPackager;
    }

    @Override
    public DataPackager getDataPackagerForDebug(DebugDataPackagerParams params) {
        final String uid = params.getDataPackagerParam().getDataPackagerCode();

        final Optional<Plugin> dataViewPluginOptional = pluginRepository.getByClass(DataViewPlugin.class);
        final Plugin dataViewPlugin = dataViewPluginOptional.orElseThrow(() -> Asserts.notFoundFail(DataViewPlugin.class.getSimpleName()));

        // create data views
        final Map<String, String> aliasCodeMapping = new HashMap<>();
        final List<DataView> raw = params.getDataViewMappingParams().stream()
            .map(item -> {
                aliasCodeMapping.put(item.getDataViewCode(), item.getDataViewAlias());

                final DebugDataViewParams debugDataViewParams = new DebugDataViewParams();
                debugDataViewParams.setDataViewCode(item.getDataViewCode());
                debugDataViewParams.setDataViewParams(item.getDataViewParams());
                debugDataViewParams.setDataSourceCode(item.getDataSourceCode());
                debugDataViewParams.setRequestDataStructure(item.getRequestDataStructure());
                debugDataViewParams.setResponseDataStructure(item.getResponseDataStructure());
                return debugDataViewParams;
            })
            .map(dataViewRepository::dataViewForDebug)
            .map(item -> {
                dataViewPlugin.mountSpi(item);
                return dataViewRepository.get(item.getIdWithVersion());
            })
            .collect(Collectors.toList());

        // create data packager
        final DataPackager dataPackager = new DataPackager(DataPackagerId.of(uid), uid,
            params.getDataPackagerParam().getScriptType(),
            params.getDataPackagerParam().getScriptContent(),
            Version.of(params.getVersion()));
        raw.forEach(dataView -> dataPackager.bindDataView(aliasCodeMapping.get(dataView.getId().getId()), dataView));
        dataPackager.bindScriptExecutionEngine(ScriptExecutionEngineHolder.INSTANCE.getByType(params.getDataPackagerParam().getScriptType()));
        dataPackager.bindRequestDataStructure(params.getDataPackagerParam().getRequestDataStructure());
        dataPackager.bindResponseDataStructure(params.getDataPackagerParam().getResponseDataStructure());

        return dataPackager;
    }

    @Override
    public void add(DataPackager dataPackager) {
        final Date date = new Date();

        synchronized (dataPackager.getIdWithVersion().toString().intern()) {
            final DataPackagerDO old = dataPackagerMapper.selectByCodeAndVersion(dataPackager.getId().getId(), dataPackager.getVersion().getId());
            if (old != null) {
                throw new DuplicateResourceException("the data packager %s has already been defined", dataPackager.getIdWithVersion().toString());
            }

            // create data packager
            final DataPackagerDO dataPackagerDO = DataPackagerAssembly.ASSEMBLY.doFromDomain(dataPackager, date);
            dataPackagerMapper.insert(dataPackagerDO);
        }

        // create data script mapping
        final DataPackagerScriptDO dataPackagerScriptDO = DataPackagerAssembly.ASSEMBLY.scriptDoFromDomain(dataPackager, date);
        dataPackagerScriptMapper.insert(dataPackagerScriptDO);

        // create data view mapping
        final List<DataPackagerViewDO> dataPackagerViewDOS = DataPackagerAssembly.ASSEMBLY.viewDosFromDomain(dataPackager, date);
        dataPackagerViewMapper.batchInsert(dataPackagerViewDOS);

        // insert req and resp datastructures into db
        final List<DataStructure> requestDataStructure = dataPackager.getRequestDataStructure().getDataStructures();
        final List<DataStructure> responseDataStructure = dataPackager.getResponseDataStructure().getDataStructures();
        dataStructureRepository.batchAdd(ImmutableMap.of(
            dataPackager.getRequestDataStructure().getId().getId() + "_" + dataPackager.getVersion().getId(), requestDataStructure,
            dataPackager.getResponseDataStructure().getId().getId() + "_" + dataPackager.getVersion().getId(), responseDataStructure
        ));

        // insert data views
        dataViewRepository.batchInsertOrUpdate(dataPackager.getDataViews().values());
    }

    @Override
    public void update(DataPackager dataPackager) {
        final Date date = new Date();
        final String dataPackagerId = dataPackager.getId().getId();
        final String version = dataPackager.getDataPackagerVersion().getId();

        synchronized (dataPackager.getIdWithVersion().toString().intern()) {
            final DataPackagerDO old = dataPackagerMapper.selectByCodeAndVersion(dataPackagerId, version);
            if (old == null) {
                throw new ResourceNotFoundException("the data packager %s not defined", dataPackager.getIdWithVersion().toString());
            }
        }

        // get old data packager views and delete no used.
        final Set<DataViewIdWithVersion> oldDataPackagerViewIds = dataPackagerViewMapper.selectByDataPackagerCodeAndVersion(dataPackagerId, version).parallelStream()
            .map(item -> DataViewIdWithVersion.of(DataViewId.of(item.getDataViewCode()), Version.of(dataPackager.getVersion().getId())))
            .collect(Collectors.toSet());
        final Set<DataViewIdWithVersion> newDataPackagerViewIds = dataPackager.getDataViews().values().parallelStream()
            .map(DataView::getIdWithVersion).collect(Collectors.toSet());
        final Collection<DataViewIdWithVersion> needToDeleteDataViews = CollectionUtils.subtract(oldDataPackagerViewIds, newDataPackagerViewIds);
        if (CollectionUtils.isNotEmpty(needToDeleteDataViews)) {
            dataViewRepository.batchDeleteByIds(needToDeleteDataViews);
        }
        if (CollectionUtils.isNotEmpty(dataPackager.getDataViews().values())) {
            // batch insert or update
            dataViewRepository.batchInsertOrUpdate(dataPackager.getDataViews().values());
        }

        // create data packager
        final DataPackagerDO dataPackagerDO = DataPackagerAssembly.ASSEMBLY.doFromDomain(dataPackager, date);
        dataPackagerMapper.updateByCodeAndVersion(dataPackagerDO);

        // create data script mapping
        final DataPackagerScriptDO dataPackagerScriptDO = DataPackagerAssembly.ASSEMBLY.scriptDoFromDomain(dataPackager, date);
        dataPackagerScriptMapper.updateByDataPackagerCodeWithVersionAndBLOBs(dataPackagerScriptDO);

        // create data view mapping
        final List<DataPackagerViewDO> dataPackagerViewDOS = DataPackagerAssembly.ASSEMBLY.viewDosFromDomain(dataPackager, date);
        dataPackagerViewMapper.deleteByPackagerCodeAndVersion(dataPackagerId, version);
        if (CollectionUtils.isNotEmpty(dataPackagerViewDOS)) {
            dataPackagerViewMapper.batchInsert(dataPackagerViewDOS);
        }

        // insert or update req and resp datastructures into db
        final List<DataStructure> requestDataStructure = dataPackager.getRequestDataStructure().getDataStructures();
        final List<DataStructure> responseDataStructure = dataPackager.getResponseDataStructure().getDataStructures();
        dataStructureRepository.batchInsertOrUpdate(ImmutableMap.of(
            dataPackager.getRequestDataStructure().getId().getId() + "_" + dataPackager.getVersion().getId(), requestDataStructure,
            dataPackager.getResponseDataStructure().getId().getId() + "_" + dataPackager.getVersion().getId(), responseDataStructure
        ));
    }

    @Override
    public void destroyDataPackagerForDebug(DataPackager dataPackager) {
        // unmount dataviews
        final Optional<Plugin> dataViewPluginOptional = pluginRepository.getByClass(DataViewPlugin.class);
        dataViewPluginOptional.ifPresent(dataViewPlugin -> dataPackager.getDataViews().values().forEach(dataViewPlugin::unmountSpi));
    }

    @Override
    public void delete(DataPackager dataPackager) {
        final DataPackagerIdWithVersion idWithVersion = dataPackager.getIdWithVersion();
        final String dataPackagerCode = idWithVersion.getDataPackagerId().getId();
        final String version = idWithVersion.getVersion().getId();

        // delete data packager and relations with script and data views
        dataPackagerMapper.deleteByCodeAndVersion(dataPackagerCode, version);
        dataPackagerScriptMapper.deleteByDataPackagerCodeAndVersion(dataPackagerCode, version);
        dataPackagerViewMapper.deleteByPackagerCodeAndVersion(dataPackagerCode, version);

        // delete data views
        dataPackager.getDataViews().values().parallelStream().forEach(dataViewRepository::delete);
    }

}
