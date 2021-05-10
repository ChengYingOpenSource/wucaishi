package com.cy.onepush.plugins.domain.context;

import com.cy.onepush.plugins.domain.classloader.PluginClassLoader;
import com.cy.onepush.plugins.domain.plugin.Plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PluginContext {

    private final Map<String, Plugin> pluginMap;
    private final PluginClassLoader pluginClassLoader;

    public PluginContext(PluginClassLoader pluginClassLoader) {
        this.pluginMap = new ConcurrentHashMap<>(8);
        this.pluginClassLoader = pluginClassLoader;
    }

    public void register(Plugin plugin, boolean override) {
        if (override) {
            pluginMap.put(plugin.getId().getId(), plugin);
        } else {
            pluginMap.putIfAbsent(plugin.getId().getId(), plugin);
        }
    }

    public <T> T getBean(Class<T> klass) {
        return null;
    }

    public PluginClassLoader getClassLoader() {
        return this.pluginClassLoader;
    }

    public void unregister(Plugin plugin) {
        pluginMap.remove(plugin.getId().getId());
    }

    public void destroy() {
        pluginMap.clear();
    }

}
