package com.cy.onepush.datapackager.infrastructure.plugin;

import com.cy.onepush.common.publishlanguage.plugin.PluginId;
import com.cy.onepush.common.utils.DomainEventUtils;
import com.cy.onepush.datapackager.domain.ScriptExecutionEngine;
import com.cy.onepush.datapackager.domain.event.DataPackagerReadyEvent;
import com.cy.onepush.datapackager.infrastructure.holder.ScriptExecutionEngineHolder;
import com.cy.onepush.plugins.domain.context.PluginContext;
import com.cy.onepush.plugins.domain.plugin.Plugin;
import com.cy.onepush.plugins.utils.ServiceLoader;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;

@Slf4j
public class ScriptExecutionEnginePlugin extends Plugin<ScriptExecutionEngine> {

    public ScriptExecutionEnginePlugin(PluginContext pluginContext) {
        super(PluginId.of("scriptExecutionEngine"), "scriptExecutionEngine", "0.1.0", ScriptExecutionEngine.class, pluginContext, 3);
    }

    @Override
    public boolean init() {
        final Iterator<Class<ScriptExecutionEngine>> iterator = ServiceLoader.load(ScriptExecutionEngine.class, getPluginContext().getClassLoader(), null).iteratorKlass();
        while (iterator.hasNext()) {
            final Class<ScriptExecutionEngine> scriptExecutionEngineKlass = iterator.next();
            try {
                // script engine init
                final ScriptExecutionEngine scriptExecutionEngine = scriptExecutionEngineKlass.newInstance();
                scriptExecutionEngine.init();

                // add into system
                ScriptExecutionEngineHolder.INSTANCE.addScriptEngine(scriptExecutionEngine);
            } catch (IllegalAccessException | InstantiationException e) {
                log.error("failed to init the script engine {}", scriptExecutionEngineKlass.getName());
            }
        }

        // publish
        DomainEventUtils.publishEvent(new DataPackagerReadyEvent(this));

        return true;
    }

    @Override
    public boolean refresh() {
        // the script execution plugin cannot be refreshed
        // due to the hard load for script engine.
        return true;
    }

    @Override
    public boolean destroy() {
        for (ScriptExecutionEngine scriptExecutionEngine : ScriptExecutionEngineHolder.INSTANCE.all()) {
            scriptExecutionEngine.destroy();
        }
        return true;
    }

    @Override
    public boolean mountSpi(ScriptExecutionEngine spi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean unmountSpi(ScriptExecutionEngine spi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ScriptExecutionEngine createSpiInstance(ScriptExecutionEngine spi) {
        throw new UnsupportedOperationException();
    }
}
