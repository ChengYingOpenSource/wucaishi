package com.cy.onepush.dataview.infrastructure.plugin;

import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.common.publishlanguage.plugin.PluginId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.common.utils.DomainEventUtils;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.datasource.domain.DataSourceRepository;
import com.cy.onepush.datasource.infrastructure.holder.DataSourceHolder;
import com.cy.onepush.datastructure.domain.DataStructureRepository;
import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.dataview.domain.event.DataViewReadyEvent;
import com.cy.onepush.dataview.infrastructure.holder.DataViewHolder;
import com.cy.onepush.dataview.infrastructure.plugin.assembly.DataViewAssembly;
import com.cy.onepush.dataview.infrastructure.repository.mybatis.bean.DataViewDO;
import com.cy.onepush.dataview.infrastructure.repository.mybatis.mapper.DataViewMapper;
import com.cy.onepush.plugins.domain.context.PluginContext;
import com.cy.onepush.plugins.domain.plugin.Plugin;
import com.cy.onepush.plugins.utils.ServiceLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class DataViewPlugin extends Plugin<DataView> {

    private final Map<DataViewId, Date> LAST_REFRESHED_TIME = new ConcurrentHashMap<>(16);

    private final ObjectMapper objectMapper;


    public DataViewPlugin(PluginContext pluginContext) {
        super(PluginId.of("dataview"), "DataView", "v0.1.0", DataView.class, pluginContext, 2);
        this.objectMapper = pluginContext.getBean(ObjectMapper.class);
    }

    @Override
    public boolean init() {
        final DataViewMapper dataViewMapper = getPluginContext().getBean(DataViewMapper.class);
        final List<DataViewDO> dataViewDOS = dataViewMapper.selectAll();

        if (CollectionUtils.isEmpty(dataViewDOS)) {
            return true;
        }

        // do to domain
        final DataSourceRepository dataSourceRepository = getPluginContext().getBean(DataSourceRepository.class);
        final DataStructureRepository dataStructureRepository = getPluginContext().getBean(DataStructureRepository.class);
        final List<DataView> dataViews = DataViewAssembly.ASSEMBLY.domainsFromDos(objectMapper, dataSourceRepository,
            dataStructureRepository, dataViewDOS);

        // init dataviews
        final boolean result = _init(dataViews, getPluginContext().getClassLoader());

        // register dataview plugin into context
        getPluginContext().register(this, true);

        // publish
        DomainEventUtils.publishEvent(new DataViewReadyEvent(this));

        return result;
    }

    @Override
    public boolean refresh() {
        final DataViewMapper dataViewMapper = getPluginContext().getBean(DataViewMapper.class);
        final List<DataViewDO> needToBeRefreshed = dataViewMapper.selectAll().parallelStream()
            .filter(dataViewDO -> {
                final Date lastRefreshedTime = LAST_REFRESHED_TIME.get(DataViewId.of(dataViewDO.getCode()));
                if (lastRefreshedTime == null) {
                    // new dataview
                    return true;
                }

                // the dataview is modified
                return dataViewDO.getGmtModified().after(lastRefreshedTime);
            })
            .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(needToBeRefreshed)) {
            // no need to be refreshed
            return true;
        }

        // do to domain
        final DataSourceRepository dataSourceRepository = getPluginContext().getBean(DataSourceRepository.class);
        final DataStructureRepository dataStructureRepository = getPluginContext().getBean(DataStructureRepository.class);
        final List<DataView> toBeRefreshed = DataViewAssembly.ASSEMBLY.domainsFromDos(objectMapper, dataSourceRepository,
            dataStructureRepository, needToBeRefreshed);

        return _init(toBeRefreshed, getPluginContext().getClassLoader());
    }

    @Override
    public boolean destroy() {
        return true;
    }

    @Override
    public boolean mountSpi(DataView spi) {
        if (DataViewHolder.INSTANCE.occupied(spi.getIdWithVersion())) {
            // occupied successfully
            return _init(Collections.singletonList(spi), getPluginContext().getClassLoader());
        }

        return false;
    }

    @Override
    public boolean unmountSpi(DataView spi) {
        DataViewHolder.INSTANCE.removeDataView(spi);
        return true;
    }

    @Override
    public DataView createSpiInstance(DataView spi) {
        final List<DataView> dataViews = _createInstance(Collections.singletonList(spi), getPluginContext().getClassLoader());
        if (CollectionUtils.isNotEmpty(dataViews)) {
            return dataViews.get(0);
        }

        return null;
    }

    private boolean _init(List<DataView> dataViews, ClassLoader classLoader) {
        final Date date = new Date();

        _createInstance(dataViews, classLoader).parallelStream()
            .filter(Objects::nonNull)
            .peek(dataView -> {
                if (!dataView.init()) {
                    log.error("the data view {} init failed", dataView.getId().getId());
                }
            })
            .peek(dataView -> {
                // cache
                DataViewHolder.INSTANCE.replaceDataView(dataView);
            })
            .forEach(dataView -> LAST_REFRESHED_TIME.put(dataView.getId(), date));

        return true;
    }

    private List<DataView> _createInstance(List<DataView> dataViews, ClassLoader classLoader) {
        final Map<String, List<DataView>> dsTypeGrouping = dataViews.stream()
            .filter(item -> item.getDataSource() != null)
            .collect(Collectors.groupingBy(this::resolveDataViewKey));

        final List<DataView> dataViewList = new ArrayList<>(dataViews.size());

        final Iterator<Class<DataView>> classIterator = ServiceLoader.load(DataView.class, classLoader, null).iteratorKlass();
        while (classIterator.hasNext()) {
            final Class<DataView> klass = classIterator.next();
            final Constructor<DataView> constructor;
            try {
                constructor = klass.getDeclaredConstructor(DataViewId.class, String.class, Version.class);
                constructor.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // will never happen. so, ignore
                continue;
            }

            // get DataView group by given group type which is the DataView class name
            final List<DataView> dsGroup = dsTypeGrouping.get(klass.getSimpleName());
            if (CollectionUtils.isEmpty(dsGroup)) {
                continue;
            }

            // create a DataView from given spec and then execute init behaviour.
            final List<DataView> partition = dsGroup.stream()
                .map(ds -> {
                    try {
                        final DataView dataView = constructor.newInstance(ds.getId(), ds.getName(), ds.getDataViewVersion());

                        // bind data view params
                        dataView.setParams(new HashMap<>(ds.getParams()));

                        // 请求和响应数据结构
                        dataView.addAllRequestDataStructure(ds.getRequestDataStructure().getDataStructures());
                        dataView.addAllResponseDataStructure(ds.getResponseDataStructure().getDataStructures());

                        // 绑定数据源
                        dataView.bindDataSource(ds.getDataSource());

                        return dataView;
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        log.error("failed to create a DataView from given spec {}", ds);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            dataViewList.addAll(partition);
        }

        return dataViewList;
    }

    private String resolveDataViewKey(DataView dataView) {
        final DataSource dataSource = DataSourceHolder.INSTANCE.getById(dataView.getDataSource().getId());

        return dataSource.getType() + DataView.class.getSimpleName();
    }

}
