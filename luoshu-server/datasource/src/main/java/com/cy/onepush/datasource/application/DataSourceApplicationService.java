package com.cy.onepush.datasource.application;

import com.cy.onepush.common.exception.Asserts;
import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.datasource.domain.DataSourceProperties;
import com.cy.onepush.datasource.domain.DataSourceRepository;
import com.cy.onepush.datasource.infrastructure.plugin.DataSourcePlugin;
import com.cy.onepush.datasource.interfaces.params.ValidateDataSourceParams;
import com.cy.onepush.plugins.domain.plugin.Plugin;
import com.cy.onepush.plugins.domain.plugin.PluginRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataSourceApplicationService {

    private final DataSourceRepository dataSourceRepository;
    private final PluginRepository pluginRepository;

    public List<DataSource> all() {
        return dataSourceRepository.allDataSources();
    }

    public List<String> allTypes() {
        final List<String> types = new ArrayList<>(dataSourceRepository.allDataSourceTypes());
        types.sort(Comparator.comparing(item -> item));

        return types;
    }

    public DataSource get(String dataSourceCode) {
        // get data source by id
        final DataSource dataSource = dataSourceRepository.getById(DataSourceId.of(dataSourceCode));
        Asserts.assertNotNull(dataSource, dataSourceCode);

        return dataSource;
    }

    public List<DataSource> batchGet(Collection<String> dataSourceCodes, String name, String type) {
        final Set<DataSourceId> dataSourceIds = dataSourceCodes.stream().map(DataSourceId::of)
            .collect(Collectors.toSet());
        return dataSourceRepository.batchGetByIds(dataSourceIds).stream()
            .filter(item -> {
                // filter
                boolean matched = true;
                if (StringUtils.isNotBlank(name)) {
                    matched = item.getName().contains(name);
                }
                if (StringUtils.isNotBlank(type)) {
                    matched &= item.getType().equalsIgnoreCase(type);
                }
                return matched;
            })
            .collect(Collectors.toList());
    }

    public boolean validate(ValidateDataSourceParams params) {
        final DataSource dataSource = dataSourceRepository.createTemp(params.getDataSourceType(), params.getDataSourceProperties());

        try {
            // 刷新并检测
            return pluginRepository.getById(DataSourcePlugin.DATASOURCE_PLUGIN_ID)
                .map(item -> (DataSource) item.createSpiInstance(dataSource))
                .map(DataSource::checkHealth)
                .orElse(false);
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    public DataSource add(String code, String name, String type, Map<String, String> dataSourceProperties) {
        // create datasource
        final DataSource dataSource = new DataSource(DataSourceId.of(code), name, type, DataSourceProperties.of(dataSourceProperties));

        // update
        dataSourceRepository.add(dataSource);

        // refresh plugin
        pluginRepository.getById(DataSourcePlugin.DATASOURCE_PLUGIN_ID)
            .ifPresent(Plugin::refresh);

        return dataSource;
    }

    @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    public DataSource update(String code, String name, String type, Map<String, String> dataSourceProperties) {
        // create datasource
        final DataSource dataSource = new DataSource(DataSourceId.of(code), name, type, DataSourceProperties.of(dataSourceProperties));

        // update
        dataSourceRepository.update(dataSource);

        // refresh plugin
        pluginRepository.getById(DataSourcePlugin.DATASOURCE_PLUGIN_ID)
            .ifPresent(Plugin::refresh);

        return dataSource;
    }

}
