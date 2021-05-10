package com.cy.onepush.plugins.extension.datasource.jdbc;

import com.cy.onepush.common.publishlanguage.datasource.DataSourceId;
import com.cy.onepush.common.publishlanguage.datastructure.DataStructureId;
import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.common.publishlanguage.executioncontext.ExecutionContextId;
import com.cy.onepush.datasource.domain.DataSourceProperties;
import com.cy.onepush.datastructure.domain.DataStructure;
import com.cy.onepush.datastructure.domain.DataType;
import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.execution.context.domain.ExecutionContext;
import com.google.common.collect.ImmutableMap;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JDBCDataSourceTest {

    private static final DataView dataView;

    @Test
    public void test_normal() {
        final Map<String, Object> mockParams = ImmutableMap.of(
            "name", "user1",
            "birthday", "2001-01-09",
            "children", Lists.newArrayList(
                ImmutableMap.of(
                    "age", 2
                )
            )
        );

        final NamedParameterJdbcTemplate jdbcTemplate = Mockito.mock(NamedParameterJdbcTemplate.class);
        Mockito.when(jdbcTemplate.queryForList(Mockito.anyString(), Mockito.refEq(mockParams))).thenReturn(Lists.newArrayList(
            ImmutableMap.of(
                "name", "user1",
                "childrenCount", 1
            )
        ));
        JDBCDataSource dataSource = Mockito.mock(JDBCDataSource.class);
        Mockito.when(dataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);

        ExecutionContext executionContext = new ExecutionContext(ExecutionContextId.of("test_context"));
        executionContext.setParams(mockParams);

        executionContext.enterContext();

        try {
            dataView.execute(executionContext);
        } finally {
            executionContext.exitContext();
        }

        final Object result = executionContext.getResult();
        System.out.println(result);
    }

    static {
        DataStructure requestField3_1 = new DataStructure(DataStructureId.of("request_field1"));
        requestField3_1.setField("age");
        requestField3_1.setDataType(DataType.INT);
        requestField3_1.setRequired(true);

        DataStructure requestField1 = new DataStructure(DataStructureId.of("request_field1"));
        requestField1.setField("name");
        requestField1.setDataType(DataType.STRING);
        requestField1.setRequired(true);
        DataStructure requestField2 = new DataStructure(DataStructureId.of("request_field1"));
        requestField2.setField("birthday");
        requestField2.setDataType(DataType.DATE);
        requestField2.setRequired(true);
        DataStructure requestField3 = new DataStructure(DataStructureId.of("request_field1"));
        requestField3.setField("children");
        requestField3.setDataType(DataType.COLLECTION);
        requestField3.setRequired(true);
        requestField3.setDataStructures(Lists.newArrayList(requestField3_1));

        DataStructure responseField1 = new DataStructure(DataStructureId.of("request_field1"));
        responseField1.setField("name");
        responseField1.setDataType(DataType.STRING);
        responseField1.setRequired(true);
        DataStructure responseField2 = new DataStructure(DataStructureId.of("request_field1"));
        responseField2.setField("childrenCount");
        responseField2.setDataType(DataType.INT);
        responseField2.setRequired(true);

        dataView = new JDBCDataView(DataViewId.of("test"), "test");
        dataView.bindDataSource(new JDBCDataSource(DataSourceId.of("test_datasource"), "test_datasource", "mysql", new DataSourceProperties(new HashMap<>())));
        dataView.addAllRequestDataStructure(Arrays.asList(requestField1, requestField2, requestField3));
        dataView.addAllResponseDataStructure(Arrays.asList(responseField1, responseField2));
        ((JDBCDataView) dataView).setCommand("select * from a");
    }

}