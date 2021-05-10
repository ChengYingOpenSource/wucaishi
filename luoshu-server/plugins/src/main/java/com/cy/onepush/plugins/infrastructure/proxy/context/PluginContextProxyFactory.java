package com.cy.onepush.plugins.infrastructure.proxy.context;

import com.cy.onepush.plugins.domain.classloader.PluginClassLoader;
import com.cy.onepush.plugins.domain.context.PluginContext;
import com.cy.onepush.plugins.domain.plugin.Plugin;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.lang.reflect.Method;
import java.util.Arrays;

public class PluginContextProxyFactory {

    private final AnnotationConfigApplicationContext applicationContext;
    private final PluginClassLoader pluginClassLoader;

    public PluginContextProxyFactory(AnnotationConfigApplicationContext applicationContext, PluginClassLoader pluginClassLoader) {
        this.applicationContext = applicationContext;
        this.pluginClassLoader = pluginClassLoader;
    }

    public PluginContext createProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(pluginClassLoader);
        enhancer.setSuperclass(PluginContext.class);
        enhancer.setCallback(new PluginContextMethodInterceptor(this.applicationContext));

        return (PluginContext) enhancer.create(new Class[]{PluginClassLoader.class}, new Object[]{pluginClassLoader});
    }

    public static class PluginContextMethodInterceptor implements MethodInterceptor {

        private final AnnotationConfigApplicationContext applicationContext;

        public PluginContextMethodInterceptor(AnnotationConfigApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            if ("register".equalsIgnoreCase(method.getName())) {
                Object result = methodProxy.invokeSuper(o, objects);
                Arrays.stream(objects)
                    .filter(item -> item instanceof Plugin)
                    .findFirst()
                    .ifPresent(item -> {
                        Plugin plugin = (Plugin) item;
                        applicationContext.getBeanFactory().registerSingleton(plugin.getId().getId(), plugin);
                    });
                return result;
            } else if ("getBean".equalsIgnoreCase(method.getName())) {
                final Class<?> klass = (Class<?>) objects[0];
                return applicationContext.getBean(klass);
            } else if ("unregister".equalsIgnoreCase(method.getName())) {
                Arrays.stream(objects)
                    .filter(item -> item instanceof Plugin)
                    .findFirst()
                    .ifPresent(item -> {
                        Plugin plugin = (Plugin) item;
                        applicationContext.getBeanFactory().destroyBean(plugin.getId().getId(), plugin);
                    });
                return methodProxy.invokeSuper(o, objects);
            } else if ("destroy".equalsIgnoreCase(method.getName())) {
                Object result = methodProxy.invokeSuper(o, objects);
                applicationContext.close();
                return result;
            } else {
                return methodProxy.invokeSuper(o, objects);
            }
        }
    }

}
