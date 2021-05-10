package com.cy.onepush.plugins.extension.script.engine.groovy.limiter;

import com.cy.onepush.datapackager.domain.ScriptExecutionLimiter;
import org.codehaus.groovy.transform.GroovyASTTransformationClass;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PACKAGE, ElementType.METHOD, ElementType.FIELD, ElementType.TYPE, ElementType.LOCAL_VARIABLE})
@GroovyASTTransformationClass("com.cy.onepush.plugins.extension.script.engine.groovy.limiter.CheckLimiterASTTransformation")
public @interface CheckLimiter {

    Class<ScriptExecutionLimiter> limiter();
    /**
     * @return handler class
     */
    Class<?> handlerClass();
    /**
     * @return handler method name
     */
    String handlerMethod();

}
