package com.cy.onepush.plugins.domain.plugin;

import com.cy.onepush.common.publishlanguage.plugin.PluginId;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PluginRepository {

    Collection<Plugin> allPlugins();

    List<Plugin> allOrderedPlugins();

    List<Plugin> allReservedOrderedPlugins();

    Optional<Plugin> getById(PluginId pluginId);

    Optional<Plugin> getByClass(Class<? extends Plugin> klass);

}
