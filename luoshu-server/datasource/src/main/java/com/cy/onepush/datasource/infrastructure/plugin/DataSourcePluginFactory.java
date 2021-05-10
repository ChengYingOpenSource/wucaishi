package com.cy.onepush.datasource.infrastructure.plugin;

import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.plugins.domain.context.PluginContext;
import com.cy.onepush.plugins.domain.plugin.PluginFactory;

public class DataSourcePluginFactory extends PluginFactory<DataSource> {

    @Override
    public DataSourcePlugin createPlugin(PluginContext pluginContext) {
        return new DataSourcePlugin(pluginContext);
    }

}
