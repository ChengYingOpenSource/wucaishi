package com.cy.onepush.plugins.infrastructure.repository;

import com.cy.onepush.plugins.domain.classloader.PluginClassLoader;
import com.cy.onepush.plugins.domain.classloader.PluginClassLoaderRepository;
import com.cy.onepush.plugins.infrastructure.PluginProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultPluginClassLoaderRepository implements PluginClassLoaderRepository {

    private final PluginProperties pluginProperties;

    private final Object PLUGIN_CLASSLOADER_LOCK = new Object();

    private PluginClassLoader pluginClassLoader;

    @Override
    public PluginClassLoader get() {
        if (pluginClassLoader != null) {
            return pluginClassLoader;
        }

        synchronized (PLUGIN_CLASSLOADER_LOCK) {
            if (pluginClassLoader != null) {
                return pluginClassLoader;
            }

            // create plugin class loader
            String rootDirPath = pluginProperties.getRootDir();
            // empty plugin root dir
            if (!StringUtils.hasLength(rootDirPath)) {
                throw new Error("please specific the plugin's root dir");
            }
            if (!StringUtils.endsWithIgnoreCase(rootDirPath, File.separator)) {
                rootDirPath = rootDirPath + File.separator;
            }

            // fetch all classes and jars
            log.info("start to load plugins under {}", rootDirPath);
            final File rootDirFile = new File(rootDirPath);
            if (!rootDirFile.exists()) {
                log.error("failed to load plugins due to bad plugins path, please check the path {} exists", rootDirPath);
                throw new Error();
            }

            final URL[] urls = FileUtils.listFiles(rootDirFile, new String[]{"jar"}, false).stream()
                .map(file -> {
                    try {
                        final URL url = file.toURI().toURL();
                        log.info("success to load plugin jar file {}", url.toString());
                        return url;
                    } catch (MalformedURLException e) {
                        log.error("the file uri is illegal", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toArray(URL[]::new);

            return (pluginClassLoader = new PluginClassLoader(urls, SpiPluginRepository.class.getClassLoader()));
        }
    }

}
