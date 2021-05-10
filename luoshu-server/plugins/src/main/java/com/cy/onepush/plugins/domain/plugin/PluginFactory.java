package com.cy.onepush.plugins.domain.plugin;

import com.cy.onepush.plugins.domain.context.PluginContext;

public abstract class PluginFactory<T> {

    public abstract Plugin<T> createPlugin(PluginContext pluginContext);

}
