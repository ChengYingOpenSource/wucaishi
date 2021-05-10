package com.cy.onepush.plugins.infrastructure.repository;

import com.cy.onepush.plugins.domain.classloader.PluginClassLoader;
import com.cy.onepush.plugins.domain.classloader.PluginClassLoaderRepository;
import com.cy.onepush.plugins.domain.context.PluginContext;
import com.cy.onepush.plugins.domain.context.PluginContextRepository;
import com.cy.onepush.plugins.infrastructure.PluginConfiguration;
import com.cy.onepush.plugins.infrastructure.proxy.context.PluginContextProxyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultPluginContextRepository implements PluginContextRepository, ApplicationContextAware, DisposableBean {

    private final Object LOCK = new Object();
    private final PluginClassLoaderRepository pluginClassLoaderRepository;

    private AnnotationConfigApplicationContext springContext = null;
    private volatile PluginContext context = null;

    private ApplicationContext parent;

    @Override
    public PluginContext getContext() {
        if (context == null) {
            synchronized (LOCK) {
                if (context == null) {
                    final PluginClassLoader pluginClassLoader = pluginClassLoaderRepository.get();
                    this.springContext = createContext(pluginClassLoader);
                    this.context = new PluginContextProxyFactory(this.springContext, pluginClassLoader).createProxy();
                }
            }
        }

        return context;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.parent = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        if (context != null) {
            synchronized (LOCK) {
                if (context != null) {
                    context = null;
                }
            }
        }
    }

    public AnnotationConfigApplicationContext createContext(PluginClassLoader pluginClassLoader) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setClassLoader(pluginClassLoader);
        context.register(PluginConfiguration.class);
        if (this.parent != null) {
            // Uses Environment from parent as well as beans
            context.setParent(this.parent);
        }
        context.refresh();
        return context;
    }

}
