package com.cy.onepush.plugins.domain.plugin;

import com.cy.onepush.common.framework.domain.AbstractAggregateRoot;
import com.cy.onepush.common.publishlanguage.plugin.PluginId;
import com.cy.onepush.plugins.domain.context.PluginContext;
import lombok.Getter;

/**
 * plugin
 *
 * @author WhatAKitty
 * @version 0.1.0
 * @date 2020-10-26
 */
@Getter
public abstract class Plugin<T> extends AbstractAggregateRoot<String> {

    /**
     * the plugin name
     */
    private final String pluginName;
    /**
     * the plugin version
     */
    private final String pluginVersion;
    /**
     * the spi class for this plugin
     */
    private final Class<T> pluginSpiClass;
    /**
     * plugin context
     */
    private final PluginContext pluginContext;
    /**
     * plugin order
     */
    private final int order;

    public Plugin(PluginId id, String pluginName, String pluginVersion, Class<T> pluginSpiClass, PluginContext pluginContext, int order) {
        super(id);
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
        this.pluginSpiClass = pluginSpiClass;
        this.pluginContext = pluginContext;
        this.order = order;
    }

    @Override
    public PluginId getId() {
        return PluginId.of(super.getId().getId());
    }

    public abstract boolean init();

    public abstract boolean refresh();

    public abstract boolean destroy();

    public abstract boolean mountSpi(T spi);

    public abstract boolean unmountSpi(T spi);

    public abstract T createSpiInstance(T spi);

}
