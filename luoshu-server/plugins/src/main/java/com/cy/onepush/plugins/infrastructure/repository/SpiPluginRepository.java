package com.cy.onepush.plugins.infrastructure.repository;

import com.cy.onepush.common.publishlanguage.plugin.PluginId;
import com.cy.onepush.plugins.domain.classloader.PluginClassLoader;
import com.cy.onepush.plugins.domain.classloader.PluginClassLoaderRepository;
import com.cy.onepush.plugins.domain.context.PluginContext;
import com.cy.onepush.plugins.domain.context.PluginContextRepository;
import com.cy.onepush.plugins.domain.plugin.Plugin;
import com.cy.onepush.plugins.domain.plugin.PluginFactory;
import com.cy.onepush.plugins.domain.plugin.PluginRepository;
import com.cy.onepush.plugins.utils.ServiceLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SpiPluginRepository implements PluginRepository {

    private final PluginClassLoaderRepository pluginClassLoaderRepository;
    private final PluginContextRepository pluginContextRepository;

    private final Map<String, Plugin> CACHED_PLUGINS = new HashMap<>();
    private final Object CACHED_LOCK = new Object();
    private volatile boolean inited = false;

    @Override
    public Collection<Plugin> allPlugins() {
        if (inited) {
            return Collections.unmodifiableCollection(CACHED_PLUGINS.values());
        }
        synchronized (CACHED_LOCK) {
            if (!inited) {
                // cache plugins
                final PluginClassLoader pluginClassLoader = pluginClassLoaderRepository.get();
                final PluginContext context = pluginContextRepository.getContext();
                final ServiceLoader<PluginFactory> serviceLoader = ServiceLoader.load(PluginFactory.class, pluginClassLoader, null);
                for (PluginFactory pluginFactory : serviceLoader) {
                    final Plugin plugin = pluginFactory.createPlugin(context);
                    final PluginId pluginId = plugin.getId();
                    CACHED_PLUGINS.put(pluginId.getId(), plugin);
                }

                inited = true;
            }

            return Collections.unmodifiableCollection(CACHED_PLUGINS.values());
        }
    }

    @Override
    public List<Plugin> allOrderedPlugins() {
        return allPlugins().stream()
            .sorted(Comparator.comparingInt(Plugin::getOrder))
            .collect(Collectors.toList());
    }

    @Override
    public List<Plugin> allReservedOrderedPlugins() {
        return allPlugins().stream()
            .sorted((a, b) -> b.getOrder() - a.getOrder())
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Plugin> getById(PluginId pluginId) {
        return Optional.ofNullable(CACHED_PLUGINS.get(pluginId.getId()));
    }

    @Override
    public Optional<Plugin> getByClass(Class<? extends Plugin> klass) {
        return CACHED_PLUGINS.values().stream()
            .filter(item -> klass.isAssignableFrom(item.getClass()))
            .findFirst();
    }

}
