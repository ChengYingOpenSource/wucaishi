package com.cy.onepush.plugins.application.listener;

import com.cy.onepush.plugins.domain.context.PluginContext;
import com.cy.onepush.plugins.domain.context.PluginContextRepository;
import com.cy.onepush.plugins.domain.plugin.Plugin;
import com.cy.onepush.plugins.domain.plugin.PluginRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationStopListener implements DisposableBean {

    private final PluginRepository pluginRepository;
    private final PluginContextRepository pluginContextRepository;

    @Override
    public void destroy() throws Exception {
        // get plugin context
        final PluginContext context = pluginContextRepository.getContext();

        // get all plugins
        final List<Plugin> plugins = pluginRepository.allReservedOrderedPlugins();
        if (plugins.isEmpty()) {
            return;
        }

        // destroy plugins
        plugins.stream()
            .peek(context::unregister)
            .forEach(plugin -> {
                if (plugin.destroy()) {
                    return;
                }

                log.error("plugin {} destroy failed", plugin.getId().getId());
            });

        // destroy context
        context.destroy();
    }
}
