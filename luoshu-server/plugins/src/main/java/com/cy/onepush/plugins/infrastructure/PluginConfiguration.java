package com.cy.onepush.plugins.infrastructure;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PluginProperties.class)
public class PluginConfiguration {
}
