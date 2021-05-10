package com.cy.onepush.datapackager.infrastructure.plugin;

import com.cy.onepush.datapackager.domain.ScriptExecutionEngine;
import com.cy.onepush.plugins.domain.context.PluginContext;
import com.cy.onepush.plugins.domain.plugin.Plugin;
import com.cy.onepush.plugins.domain.plugin.PluginFactory;

public class ScriptExecutionEnginePluginFactory extends PluginFactory<ScriptExecutionEngine> {
    @Override
    public Plugin<ScriptExecutionEngine> createPlugin(PluginContext pluginContext) {
        return new ScriptExecutionEnginePlugin(pluginContext);
    }
}
