package com.cy.onepush.datasource.infrastructure.plugin;

import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.common.publishlanguage.plugin.PluginId;
import com.cy.onepush.common.utils.DomainEventUtils;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.datasource.domain.DataSourceProperties;
import com.cy.onepush.datasource.domain.event.DataSourceReadyEvent;
import com.cy.onepush.datasource.infrastructure.holder.DataSourceHolder;
import com.cy.onepush.datasource.infrastructure.repository.mybatis.bean.DataSourceDO;
import com.cy.onepush.datasource.infrastructure.repository.mybatis.mapper.DataSourceMapper;
import com.cy.onepush.plugins.domain.context.PluginContext;
import com.cy.onepush.plugins.domain.plugin.Plugin;
import com.cy.onepush.plugins.utils.ServiceLoader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public class DataSourcePlugin extends Plugin<DataSource> {

    public static final PluginId DATASOURCE_PLUGIN_ID = PluginId.of("DataSourcePlugin");

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<DataSourceId, Date> LAST_REFRESHED_TIME = new ConcurrentHashMap<>(16);

    public DataSourcePlugin(PluginContext pluginContext) {
        super(DATASOURCE_PLUGIN_ID, "DataSourcePlugin", "v0.1.0", DataSource.class, pluginContext, 1);
    }

    @Override
    public boolean init() {
        final DataSourceMapper dataSourceMapper = getPluginContext().getBean(DataSourceMapper.class);
        final List<DataSourceDO> dataSourceDOS = dataSourceMapper.selectAll();
        if (CollectionUtils.isEmpty(dataSourceDOS)) {
            return true;
        }

        // init datasource
        final boolean result = _init(dataSourceDOS, getPluginContext().getClassLoader());

        // register datasource plugin into context
        getPluginContext().register(this, true);

        // publish
        DomainEventUtils.publishEvent(new DataSourceReadyEvent(this));

        return result;
    }

    @Override
    public boolean refresh() {
        final DataSourceMapper dataSourceMapper = getPluginContext().getBean(DataSourceMapper.class);
        final List<DataSourceDO> needToBeRefreshed = dataSourceMapper.selectAll().parallelStream()
            .filter(dataSourceDO -> {
                final Date lastRefreshedTime = LAST_REFRESHED_TIME.get(DataSourceId.of(dataSourceDO.getCode()));
                if (lastRefreshedTime == null) {
                    // new datasource
                    return true;
                }

                // the datasource is modified
                return dataSourceDO.getGmtModified().after(lastRefreshedTime);
            })
            .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(needToBeRefreshed)) {
            // no need to be refreshed
            return true;
        }
        return _init(needToBeRefreshed, getPluginContext().getClassLoader());
    }

    @Override
    public boolean destroy() {
        return DataSourceHolder.INSTANCE.all().stream()
            .allMatch(dataSource -> {
                if (dataSource.destroy()) {
                    return true;
                }

                log.error("the datasource {} cannot destroy", dataSource.getId().getId());
                return false;
            });
    }

    @Override
    public boolean mountSpi(DataSource spi) {
        DataSourceDO dataSourceDO = new DataSourceDO();
        dataSourceDO.setCode(spi.getId().getId());
        dataSourceDO.setConf(spi.getProperties().toString());
        dataSourceDO.setDsType(spi.getType());
        dataSourceDO.setName(spi.getName());

        return _init(Collections.singletonList(dataSourceDO), getPluginContext().getClassLoader());
    }

    @Override
    public boolean unmountSpi(DataSource spi) {
        DataSourceHolder.INSTANCE.removeDataSource(spi);
        return true;
    }

    @Override
    public DataSource createSpiInstance(DataSource spi) {
        DataSourceDO dataSourceDO = new DataSourceDO();
        dataSourceDO.setCode(spi.getId().getId());
        dataSourceDO.setConf(spi.getProperties().toString());
        dataSourceDO.setDsType(spi.getType());
        dataSourceDO.setName(spi.getName());

        final List<DataSource> dataSources = _createInstance(Collections.singletonList(dataSourceDO), getPluginContext().getClassLoader());
        if (CollectionUtils.isNotEmpty(dataSources)) {
            return dataSources.get(0);
        }

        return null;
    }

    private boolean _init(List<DataSourceDO> dataSourceDOS, ClassLoader classLoader) {
        final Date date = new Date();
        _createInstance(dataSourceDOS, classLoader).parallelStream()
            .filter(dataSource -> {
                if (!dataSource.init()) {
                    log.error("the datasource {} cannot init", dataSource.getId().getId());
                    return false;
                }

                // get old datasource
                final DataSource oldDataSource = DataSourceHolder.INSTANCE.getById(dataSource.getId());

                // replace the cache
                DataSourceHolder.INSTANCE.replaceDataSource(dataSource);

                // clean pre cache
                if (oldDataSource != null) {
                    if (!oldDataSource.destroy()) {
                        log.error("the old datasource {} destroy failed", oldDataSource.getId().getId());
                    }
                }

                return true;
            })
            .forEach(dataSource -> LAST_REFRESHED_TIME.put(dataSource.getId(), date));

        return true;
    }

    private List<DataSource> _createInstance(List<DataSourceDO> dataSourceDOS, ClassLoader classLoader) {
        final Map<String, List<DataSourceDO>> dsTypeGrouping = dataSourceDOS.stream().collect(Collectors.groupingBy(this::resolveDataSourceKey));

        final TypeReference<HashMap<String, String>> mapTypeRef = new TypeReference<HashMap<String, String>>() {
        };

        final List<DataSource> dataSources = new ArrayList<>(dataSourceDOS.size());
        final Iterator<Class<DataSource>> classIterator = ServiceLoader.load(DataSource.class, classLoader, null).iteratorKlass();
        while (classIterator.hasNext()) {
            final Class<DataSource> klass = classIterator.next();
            final Constructor<DataSource> constructor;
            try {
                constructor = klass.getDeclaredConstructor(DataSourceId.class, String.class, String.class, DataSourceProperties.class);
                constructor.setAccessible(true);
            } catch (NoSuchMethodException e) {
                // will never happen. so, ignore
                continue;
            }

            // get datasource group by given group type which is the datasource class name
            final List<DataSourceDO> dsGroup = dsTypeGrouping.get(klass.getSimpleName());
            if (CollectionUtils.isEmpty(dsGroup)) {
                continue;
            }

            // create a datasource from given spec and then execute init behaviour.
            final List<DataSource> partition = dsGroup.stream()
                .map(ds -> {
                    final Map<String, String> map;
                    try {
                        map = objectMapper.readValue(ds.getConf(), mapTypeRef);
                    } catch (JsonProcessingException e) {
                        log.error("failed to parse the datasource-{}'s properties {}", ds.getId(), ds.getConf());
                        return null;
                    }
                    final DataSourceProperties dataSourceProperties = DataSourceProperties.of(map);
                    try {
                        return constructor.newInstance(DataSourceId.of(ds.getCode()), ds.getName(), ds.getDsType(), dataSourceProperties);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        log.error("failed to create a datasource from given spec {}", ds);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

            dataSources.addAll(partition);
        }

        return dataSources;
    }

    private String resolveDataSourceKey(DataSourceDO dataSourceDO) {
        return dataSourceDO.getDsType() + DataSource.class.getSimpleName();
    }

}
