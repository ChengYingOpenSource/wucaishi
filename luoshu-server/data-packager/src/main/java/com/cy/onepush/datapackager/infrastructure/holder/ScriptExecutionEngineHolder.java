package com.cy.onepush.datapackager.infrastructure.holder;

import com.cy.onepush.datapackager.domain.ScriptExecutionEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ScriptExecutionEngineHolder {

    public static ScriptExecutionEngineHolder INSTANCE = new ScriptExecutionEngineHolder();

    private final Map<String, ScriptExecutionEngine> HOLDER = new ConcurrentHashMap<>(8);

    public void addScriptEngine(ScriptExecutionEngine scriptExecutionEngine) {
        HOLDER.put(StringUtils.lowerCase(scriptExecutionEngine.getType()), scriptExecutionEngine);
    }

    public Collection<ScriptExecutionEngine> all() {
        return Collections.unmodifiableCollection(HOLDER.values());
    }

    public Set<String> allTypes() {
        return Collections.unmodifiableSet(HOLDER.keySet().stream().map(String::toLowerCase).collect(Collectors.toSet()));
    }

    public ScriptExecutionEngine getByType(String type) {
        return HOLDER.get(StringUtils.lowerCase(type));
    }

    public boolean isSupport(String type) {
        return HOLDER.containsKey(StringUtils.lowerCase(type));
    }

}
