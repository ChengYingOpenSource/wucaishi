package com.cy.onepush.plugins.extension.script.engine.groovy;

import com.cy.onepush.common.publishlanguage.console.ConsoleId;
import com.cy.onepush.common.publishlanguage.datastructure.DataStructureId;
import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.common.publishlanguage.debugtool.DebugToolId;
import com.cy.onepush.common.publishlanguage.executioncontext.ExecutionContextId;
import com.cy.onepush.console.domain.Console;
import com.cy.onepush.datapackager.domain.*;
import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.datastructure.domain.DataStructure;
import com.cy.onepush.datastructure.domain.DataType;
import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.debugtool.domain.DebugTool;
import com.cy.onepush.execution.context.domain.ExecutionContext;
import com.google.common.collect.ImmutableMap;
import groovy.lang.MissingPropertyException;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.mockito.Mockito;
import org.testng.Assert;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroovyScriptExecutionEngineTest {

    @Test
    public void test_normal() {
        ExecutionContext testContext = new ExecutionContext(ExecutionContextId.of("test"), true, null);

        ScriptExecutionEngine engine = new GroovyScriptExecutionEngine();
        final Object result = engine.execute(testContext, ImmutableMap.of(), ExecutionScript.of("groovy", "return 1;"), new HashMap<>());

        Assert.assertEquals(1, result);
    }

    @Test(expected = MissingPropertyException.class)
    public void test_bad_grammar() {
        ExecutionContext testContext = new ExecutionContext(ExecutionContextId.of("test"), true, null);

        ScriptExecutionEngine engine = new GroovyScriptExecutionEngine();
        engine.execute(testContext, ImmutableMap.of(), ExecutionScript.of("groovy", "return x;"), new HashMap<>());
    }

    @Test
    public void test_dataviewers() {
        ExecutionContext testContext = new ExecutionContext(ExecutionContextId.of("test"), true, null);

        DataView dataView = new SimpleMockDataView();

        ScriptExecutionEngine engine = new GroovyScriptExecutionEngine();
        final Object result = engine.execute(testContext,
            ImmutableMap.of(
                "test", dataView
            ),
            ExecutionScript.of("groovy", "return dataViewers.get(\"test\");"), new HashMap<>());

        Assert.assertEquals(1, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void test_complex_dataviewers() {
        ExecutionContext testContext = new ExecutionContext(ExecutionContextId.of("test"), true, null);

        DataView dataView = new ComplexMockDataView();

        ScriptExecutionEngine engine = new GroovyScriptExecutionEngine();
        final Object result = engine.execute(testContext,
            ImmutableMap.of(
                "test", dataView
            ),
            ExecutionScript.of("groovy", "return dataViewers.get(\"test\");"), new HashMap<>());

        final Map<String, Object> map = (Map<String, Object>) result;
        final Map<String, Object> data = (Map<String, Object>) map.get("data");
        final Integer a = (Integer) data.get("a");
        final List<Map<String, Object>> b = (List<Map<String, Object>>) data.get("b");
        final Date c = (Date) b.get(0).get("c");
        Assert.assertEquals(2, data.size());
        Assert.assertEquals(1, a.intValue());
        Assert.assertEquals(1, b.size());
        Assert.assertEquals("2020-12-11", DateFormatUtils.format(c, "yyyy-MM-dd"));
    }

    @Test
    public void test_watchable() {
        Console console = new Console(ConsoleId.of("test"));
        DebugTool debugTool = new DebugTool(DebugToolId.of("test"), console);
        ExecutionContext testContext = new ExecutionContext(ExecutionContextId.of("test"), true, debugTool);
        testContext.setParams(new HashMap<>());

        DataView dataView = new SimpleMockDataView();

        ScriptExecutionLimiter limiter = new ScriptExecutionLimiter(LimitExceedStrategy.REJECT, LimitRule.of("1", "true"));
        ScriptExecutionEngine engine = new ScriptExecutionLimiter.ScriptExecutionWatchableEngine(new GroovyScriptExecutionEngine(), limiter);
        final Object result = engine.execute(testContext,
            ImmutableMap.of(
                "test", dataView
            ),
            ExecutionScript.of("groovy", "return dataViewers.get(\"test\");"), new HashMap<>());

        Assert.assertEquals(1, result);
    }

    private abstract static class AbstractMockDataView extends DataView {

        private final Object result;

        public AbstractMockDataView(Object result) {
            super(DataViewId.of("mock"), "mock");
            this.result = result;

            bindDataSource(Mockito.mock(DataSource.class));
        }

        @Override
        protected Object _execute(DataSource dataSource, Object params) {
            return result;
        }
    }

    private static class SimpleMockDataView extends AbstractMockDataView {

        public SimpleMockDataView() {
            super(1);

            final DataStructure responseDataStructure = new DataStructure(DataStructureId.of("response"));
            responseDataStructure.setName("a");
            responseDataStructure.setField("a");
            responseDataStructure.setDataType(DataType.INT);
            responseDataStructure.setRequired(true);
            addResponseDataStructure(responseDataStructure);
        }
    }

    private static class ComplexMockDataView extends AbstractMockDataView {

        public ComplexMockDataView() {
            super(ImmutableMap.of(
                "a", 1,
                "b", Lists.newArrayList(
                    ImmutableMap.of(
                        "c", "2020-12-11"
                    )
                )));

            final DataStructure c = new DataStructure(DataStructureId.of("a"));
            c.setDataType(DataType.DATE);
            c.setName("c");
            c.setField("c");
            c.setRequired(true);

            final DataStructure a = new DataStructure(DataStructureId.of("a"));
            a.setDataType(DataType.INT);
            a.setName("a");
            a.setField("a");
            a.setRequired(true);
            final DataStructure b = new DataStructure(DataStructureId.of("a"));
            b.setDataType(DataType.COLLECTION);
            b.setName("b");
            b.setField("b");
            b.setRequired(true);
            b.setDataStructures(Lists.newArrayList(c));

            addAllResponseDataStructure(Lists.newArrayList(a, b));
        }
    }

}