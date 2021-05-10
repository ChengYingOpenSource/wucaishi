package com.cy.onepush.plugins.application.listener;

import com.cy.onepush.plugins.domain.context.PluginContext;
import com.cy.onepush.plugins.domain.context.PluginContextRepository;
import com.cy.onepush.plugins.domain.plugin.Plugin;
import com.cy.onepush.plugins.domain.plugin.PluginRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationInitListener implements ApplicationListener<ApplicationStartedEvent> {

    private final PluginRepository pluginRepository;
    private final PluginContextRepository pluginContextRepository;

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent applicationStartedEvent) {
        // get all plugins
        final List<Plugin> plugins = pluginRepository.allOrderedPlugins();
        if (plugins.isEmpty()) {
            return;
        }

        // init plugins
        plugins.forEach(plugin -> {
            if (plugin.init()) {
                log.info("plugin {} init successfully", plugin.getId().getId());
                return;
            }

            log.error("plugin {} init failed", plugin.getId().getId());
        });
    }
}
