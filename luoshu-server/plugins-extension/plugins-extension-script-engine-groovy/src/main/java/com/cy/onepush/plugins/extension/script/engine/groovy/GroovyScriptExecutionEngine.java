package com.cy.onepush.plugins.extension.script.engine.groovy;

import com.cy.onepush.datapackager.domain.ExecutionScript;
import com.cy.onepush.datapackager.domain.ScriptExecutionEngine;
import com.cy.onepush.debugtool.domain.DebugTool;
import com.cy.onepush.execution.context.domain.ExecutionContext;
import com.cy.onepush.plugins.extension.script.engine.groovy.limiter.CheckLimiter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.collect.ImmutableMap;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class GroovyScriptExecutionEngine extends ScriptExecutionEngine {

    private static final Script empty = new Script() {
        public Object run() {
            return null;
        }
    };

    private static final AtomicLong scriptCount = new AtomicLong(0);

    private final Map<ScriptKey, ASTTransformationCustomizer> checkMapping = new ConcurrentHashMap<>(300);

    private final CacheLoader<ScriptKey, Script> cacheLoader = new CacheLoader<ScriptKey, Script>() {
        @Override
        public Script load(ScriptKey key) throws Exception {

            String md5Key = key.getMd5Key();
            String keyMd5HashCode = String.valueOf(Math.abs(md5Key.hashCode()));
            String scriptNumHashCode = String.valueOf(scriptCount.incrementAndGet() % 10000);

            CompilerConfiguration cc = new CompilerConfiguration();
            if (checkMapping.get(key) != null) {
                cc.addCompilationCustomizers(checkMapping.get(key));
            }
            String classpath = "com.cy.onepush.plugins.extension.script.engine.groovy.script"; // File.pathSeparator
            // 设置父类是否会产生很多 Delete..ClassLoader?
            //cc.setScriptBaseClass("com.cy.dataservice.engine.func.MFunc");
            cc.setClasspath(classpath);
            // fileName 设置;
            // 如果不指定名称，则生成的Script名字都是一样的， 一个classloader只能加载同名的class一次，所以会生成大量的InnerLoader
            String scriptName = "Script".concat(keyMd5HashCode).concat(scriptNumHashCode);

            return new GroovyShell(cc).parse(key.getNoteContent(), scriptName);
        }

    };

    private final LoadingCache<ScriptKey, Script> scriptCache = CacheBuilder.newBuilder()
        .maximumSize(300) // 不要太大，容易GC
        // 1天内没放完则过期
        .expireAfterAccess(3, TimeUnit.DAYS).removalListener((RemovalListener<ScriptKey, Script>) notification -> {
            Script script = notification.getValue();
            if (script != empty && script != null) {
                InvokerHelper.removeClass(script.getClass());
            }
        })
        .build(cacheLoader);

    @Override
    public String getType() {
        return "Groovy";
    }

    @Override
    public void destroy() {
        // reset count
        scriptCount.set(0L);
        // cache clean
        scriptCache.invalidateAll();
        // log print
        log.info("the groovy script execution engine has been destroyed");
    }

    @Override
    public void _init() {

    }

    @Override
    public boolean _check(ExecutionContext context, ExecutionScript script) {
        return true;
    }

    @Override
    public Object _execute(ExecutionContext context,
                           ExecutionScript script,
                           Map<String, Object> params,
                           Map<String, Object> functions) {
        final boolean isDebug = context.isDebug();
        final DebugTool debugTool = context.getDebugTool();

        final ScriptKey scriptKey = new ScriptKey(script.getType(), script.getContent(),
            DigestUtils.md5DigestAsHex(script.getContent().getBytes(StandardCharsets.UTF_8)));

        final Binding binding = buildBind(params, functions);

        // load checker
        loadChecker(scriptKey);

        final Script shell;
        try {
            if (isDebug) {
                final Script temp = cacheLoader.load(scriptKey);

                shell = InvokerHelper.createScript(temp.getClass(), binding);
            } else {
                shell = scriptCache.get(scriptKey);
                shell.setBinding(binding);
            }
        } catch (ExecutionException e) {
            log.warn("execution script failed", e);
            debugTool.printError(e, "execution script failed with stack");
            return null;
        } catch (Exception e) {
            log.error("failed to execute script with exception ", e);
            debugTool.printError(e, "failed to execute script with exception ");
            return null;
        }

        try {
            return shell.run();
        } catch (Error e) {
            log.error("failed to run groovy script", e);
            debugTool.printError(e, "failed to run groovy script");
            throw e;
        }
    }

    private void loadChecker(ScriptKey scriptKey) {
        checkMapping.compute(scriptKey, (k, old) -> {
            if (old != null) {
                return old;
            }

            return new ASTTransformationCustomizer(ImmutableMap.of(), CheckLimiter.class);
        });
    }

    private Binding buildBind(Map<String, Object> params, Map<String, Object> functions) {
        Binding binding = new Binding();
        // bind parameters
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String variableName = entry.getKey();
                Object value = entry.getValue();
                binding.setVariable(variableName, value);
            }
        }
        // bind functions
        if (functions != null) {
            for (Map.Entry<String, Object> entry : functions.entrySet()) {
                String variableName = entry.getKey();
                Object value = entry.getValue();
                binding.setVariable(variableName, value);
            }
        }
        return binding;
    }

    @Data
    @AllArgsConstructor
    public static class ScriptKey {

        public String noteKey;

        public String noteContent;

        public String md5Key;

        public int hashCode() {

            return noteKey.hashCode() | md5Key.hashCode();
        }

        @Override
        public boolean equals(Object other) {

            if (!(other instanceof ScriptKey)) {
                return false;
            }
            ScriptKey oKey = (ScriptKey) other;
            String onotekey = oKey.getNoteKey();
            String omd5 = oKey.getMd5Key();
            if (this.noteKey.equals(onotekey) && this.md5Key.equals(omd5)) {
                return true;
            }

            return false;
        }


    }

}
