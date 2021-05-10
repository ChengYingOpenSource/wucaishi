package com.cy.onepush.datastructure.domain;

import com.cy.onepush.common.publishlanguage.datastructure.DataStructureId;
import com.google.common.collect.ImmutableMap;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class DataStructureTest {

    private static final DataStructure dataStructure;

    @Test
    public void test_normal() {
        final boolean result = dataStructure.validate(ImmutableMap.of(
            "field1", 1,
            "field2", Lists.newArrayList(
                ImmutableMap.of(
                    "field2_1", "2020-12-09",
                    "field2_2", "test2_2",
                    "field2_3", 1.345,
                    "field2_4", ImmutableMap.of(
                        "field2_4_1", 123
                    )
                )
            )
        ));

        Assert.assertTrue(result);
    }

    @Test
    public void test_failed() {
        final boolean result = dataStructure.validate(ImmutableMap.of(
            "field1", 1,
            "field2", Lists.newArrayList(
                ImmutableMap.of(
                    "field2_1", "123",
                    "field2_2", "test2_2",
                    "field2_3", 1.345,
                    "field2_4", ImmutableMap.of(
                        "field2_4_1", 123
                    )
                )
            )
        ));

        Assert.assertFalse(result);
    }

    @Test
    public void test_failed_with_bad_structure() {
        final boolean result = dataStructure.validate(ImmutableMap.of(
            "field1", 1,
            "field2", Lists.newArrayList(
                ImmutableMap.of(
                    "field2_1", "2020-12-09",
                    "field2_2", "test2_2",
                    "field2_3", 1.345,
                    "field2_4", ImmutableMap.of(
                    )
                )
            )
        ));

        Assert.assertFalse(result);
    }

    static {
        DataStructure field2_4_1 = new DataStructure(DataStructureId.of("field2_4_1"));
        field2_4_1.setDataType(DataType.INT);
        field2_4_1.setField("field2_4_1");
        field2_4_1.setName("field2_4_1");
        field2_4_1.setRequired(true);

        DataStructure field2_1 = new DataStructure(DataStructureId.of("field2_1"));
        field2_1.setDataType(DataType.DATE);
        field2_1.setField("field2_1");
        field2_1.setName("field2_1");
        DataStructure field2_2 = new DataStructure(DataStructureId.of("field2_2"));
        field2_2.setDataType(DataType.STRING);
        field2_2.setField("field2_2");
        field2_2.setName("field2_2");
        DataStructure field2_3 = new DataStructure(DataStructureId.of("field2_3"));
        field2_3.setDataType(DataType.DECIMAL);
        field2_3.setField("field2_3");
        field2_3.setName("field2_3");
        DataStructure field2_4 = new DataStructure(DataStructureId.of("field2_4"));
        field2_4.setDataType(DataType.ITEM);
        field2_4.setField("field2_4");
        field2_4.setName("field2_4");
        field2_4.getDataStructures().add(field2_4_1);

        DataStructure field1 = new DataStructure(DataStructureId.of("field1"));
        field1.setDataType(DataType.INT);
        field1.setField("field1");
        field1.setName("field1");
        DataStructure field2 = new DataStructure(DataStructureId.of("field2"));
        field2.setDataType(DataType.COLLECTION);
        field2.setField("field2");
        field2.setName("field2");
        field2.getDataStructures().addAll(Arrays.asList(field2_1, field2_2, field2_3, field2_4));

        dataStructure = new DataStructure(DataStructureId.of("test"));
        dataStructure.setDataType(DataType.ITEM);
        dataStructure.setField("data");
        dataStructure.setRequired(true);
        dataStructure.setName("data");
        dataStructure.getDataStructures().addAll(Arrays.asList(field1, field2));
    }

}