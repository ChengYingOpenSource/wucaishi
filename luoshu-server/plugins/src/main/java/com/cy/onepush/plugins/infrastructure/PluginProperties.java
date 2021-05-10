package com.cy.onepush.plugins.infrastructure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "luoshu.plugin")
public class PluginProperties {

    private String rootDir;

}
