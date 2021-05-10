package com.cy.onepush.dataview.infrastructure.plugin;

import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.plugins.domain.context.PluginContext;
import com.cy.onepush.plugins.domain.plugin.PluginFactory;

public class DataViewPluginFactory extends PluginFactory<DataView> {

    @Override
    public DataViewPlugin createPlugin(PluginContext pluginContext) {
        return new DataViewPlugin(pluginContext);
    }

}
