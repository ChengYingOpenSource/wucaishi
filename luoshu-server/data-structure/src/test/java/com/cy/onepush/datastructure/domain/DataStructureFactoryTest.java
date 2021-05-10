package com.cy.onepush.datastructure.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DataStructureFactoryTest {

    @Test
    public void test_single_field() {
        final Optional<List<DataStructure>> dataStructuresOptional = DataStructureFactory.createFromMap("request", ImmutableList.of(ImmutableMap.of(
            "dataType", "INT",
            "name", "field1",
            "field", "field1",
            "required", true
        )));
        Assert.assertTrue(dataStructuresOptional.isPresent());

        final List<DataStructure> dataStructures = dataStructuresOptional.get();

        Assert.assertNotNull(dataStructures);
        Assert.assertEquals(1, dataStructures.size());

        final DataStructure dataStructure = dataStructures.get(0);
        Assert.assertEquals(DataType.INT, dataStructure.getDataType());
        Assert.assertEquals("field1", dataStructure.getField());
        Assert.assertEquals("field1", dataStructure.getName());
        Assert.assertTrue(dataStructure.isRequired());
        Assert.assertEquals(0, dataStructure.getDataStructures().size());
    }

    @Test
    public void test_multi_field() {
        final Optional<List<DataStructure>> dataStructuresOptional = DataStructureFactory.createFromMap("request", ImmutableList.of(
            ImmutableMap.of(
                "dataType", "INT",
                "name", "field1",
                "field", "field1",
                "required", true
            ),
            ImmutableMap.of(
                "dataType", "DATE",
                "name", "field2",
                "field", "field2",
                "required", false
            )
        ));
        Assert.assertTrue(dataStructuresOptional.isPresent());

        final List<DataStructure> dataStructures = dataStructuresOptional.get();

        Assert.assertNotNull(dataStructures);
        Assert.assertEquals(2, dataStructures.size());

        final DataStructure field1 = dataStructures.get(0);
        Assert.assertEquals(DataType.INT, field1.getDataType());
        Assert.assertEquals("field1", field1.getField());
        Assert.assertEquals("field1", field1.getName());
        Assert.assertTrue(field1.isRequired());
        Assert.assertEquals(0, field1.getDataStructures().size());

        final DataStructure field2 = dataStructures.get(1);
        Assert.assertEquals(DataType.DATE, field2.getDataType());
        Assert.assertEquals("field2", field2.getField());
        Assert.assertEquals("field2", field2.getName());
        Assert.assertFalse(field2.isRequired());
        Assert.assertEquals(0, field2.getDataStructures().size());
    }

    @Test
    public void test_inner_item_fields() {
        final Optional<List<DataStructure>> dataStructuresOptional = DataStructureFactory.createFromMap("request", ImmutableList.of(
            ImmutableMap.of(
                "dataType", "ITEM",
                "name", "field1",
                "field", "field1",
                "required", true,
                "dataStructures", ImmutableList.of(
                    ImmutableMap.of(
                        "dataType", "DATE",
                        "name", "field1_1",
                        "field", "field1_1",
                        "required", true
                    ),
                    ImmutableMap.of(
                        "dataType", "DECIMAL",
                        "name", "field1_2",
                        "field", "field1_2",
                        "required", false
                    )
                )
            )
        ));
        Assert.assertTrue(dataStructuresOptional.isPresent());

        final List<DataStructure> dataStructures = dataStructuresOptional.get();

        Assert.assertNotNull(dataStructures);
        Assert.assertEquals(1, dataStructures.size());

        final DataStructure dataStructure = dataStructures.get(0);
        Assert.assertEquals(DataType.ITEM, dataStructure.getDataType());
        Assert.assertEquals("field1", dataStructure.getField());
        Assert.assertEquals("field1", dataStructure.getName());
        Assert.assertTrue(dataStructure.isRequired());
        Assert.assertEquals(2, dataStructure.getDataStructures().size());

        final DataStructure field1_1 = dataStructures.get(0).getDataStructures().get(0);
        Assert.assertEquals(DataType.DATE, field1_1.getDataType());
        Assert.assertEquals("field1_1", field1_1.getField());
        Assert.assertEquals("field1_1", field1_1.getName());
        Assert.assertTrue(field1_1.isRequired());

        final DataStructure field1_2 = dataStructures.get(0).getDataStructures().get(1);
        Assert.assertEquals(DataType.DECIMAL, field1_2.getDataType());
        Assert.assertEquals("field1_2", field1_2.getField());
        Assert.assertEquals("field1_2", field1_2.getName());
        Assert.assertFalse(field1_2.isRequired());
    }

    @Test
    public void test_inner_item_inner_collection_fields() {
        final List<Map<String, Object>> map = ImmutableList.of(
            ImmutableMap.of(
                "dataType", "ITEM",
                "name", "field1",
                "field", "field1",
                "required", true,
                "dataStructures", ImmutableList.of(
                    ImmutableMap.of(
                        "dataType", "DATE",
                        "name", "field1_1",
                        "field", "field1_1",
                        "required", true
                    ),
                    ImmutableMap.of(
                        "dataType", "ITEM",
                        "name", "field1_2",
                        "field", "field1_2",
                        "required", false,
                        "dataStructures", ImmutableList.of(
                            ImmutableMap.of(
                                "dataType", "COLLECTION",
                                "name", "field1_2_1",
                                "field", "field1_2_1",
                                "required", true,
                                "dataStructures", ImmutableList.of(
                                    ImmutableMap.of(
                                        "dataType", "INT",
                                        "name", "field1_2_1_1",
                                        "field", "field1_2_1_1",
                                        "required", false
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            ImmutableMap.of(
                "dataType", "COLLECTION",
                "name", "field2",
                "field", "field2",
                "required", true,
                "dataStructures", ImmutableList.of(
                    ImmutableMap.of(
                        "dataType", "DATE",
                        "name", "field2_1",
                        "field", "field2_1",
                        "required", true
                    ),
                    ImmutableMap.of(
                        "dataType", "DECIMAL",
                        "name", "field2_2",
                        "field", "field2_2",
                        "required", false
                    )
                )
            ));

        try {
            System.out.println(new ObjectMapper().writeValueAsString(map));
        } catch (JsonProcessingException e) {
            Assert.fail("failed to write json");
        }

        final Optional<List<DataStructure>> dataStructuresOptional = DataStructureFactory.createFromMap("request", map);
        Assert.assertTrue(dataStructuresOptional.isPresent());

        final List<DataStructure> dataStructures = dataStructuresOptional.get();

        Assert.assertNotNull(dataStructures);
        Assert.assertEquals(2, dataStructures.size());

        final DataStructure field1 = dataStructures.get(0);
        Assert.assertEquals(DataType.ITEM, field1.getDataType());
        Assert.assertEquals("field1", field1.getField());
        Assert.assertEquals("field1", field1.getName());
        Assert.assertTrue(field1.isRequired());
        Assert.assertEquals(2, field1.getDataStructures().size());

        final DataStructure field1_1 = field1.getDataStructures().get(0);
        Assert.assertEquals(DataType.DATE, field1_1.getDataType());
        Assert.assertEquals("field1_1", field1_1.getField());
        Assert.assertEquals("field1_1", field1_1.getName());
        Assert.assertTrue(field1_1.isRequired());

        final DataStructure field1_2 = field1.getDataStructures().get(1);
        Assert.assertEquals(DataType.ITEM, field1_2.getDataType());
        Assert.assertEquals("field1_2", field1_2.getField());
        Assert.assertEquals("field1_2", field1_2.getName());
        Assert.assertFalse(field1_2.isRequired());
        Assert.assertEquals(1, field1_2.getDataStructures().size());

        final DataStructure field1_2_1 = field1_2.getDataStructures().get(0);
        Assert.assertEquals(DataType.COLLECTION, field1_2_1.getDataType());
        Assert.assertEquals("field1_2_1", field1_2_1.getField());
        Assert.assertEquals("field1_2_1", field1_2_1.getName());
        Assert.assertTrue(field1_2_1.isRequired());
        Assert.assertEquals(1, field1_2_1.getDataStructures().size());

        final DataStructure field1_2_1_1 = field1_2_1.getDataStructures().get(0);
        Assert.assertEquals(DataType.INT, field1_2_1_1.getDataType());
        Assert.assertEquals("field1_2_1_1", field1_2_1_1.getField());
        Assert.assertEquals("field1_2_1_1", field1_2_1_1.getName());
        Assert.assertFalse(field1_2_1_1.isRequired());
        Assert.assertEquals(0, field1_2_1_1.getDataStructures().size());

        final DataStructure field2 = dataStructures.get(0);
        Assert.assertEquals(DataType.ITEM, field2.getDataType());
        Assert.assertEquals("field2", field2.getField());
        Assert.assertEquals("field2", field2.getName());
        Assert.assertTrue(field2.isRequired());
        Assert.assertEquals(2, field2.getDataStructures().size());

        final DataStructure field2_1 = dataStructures.get(0).getDataStructures().get(0);
        Assert.assertEquals(DataType.DATE, field2_1.getDataType());
        Assert.assertEquals("field2_1", field2_1.getField());
        Assert.assertEquals("field2_1", field2_1.getName());
        Assert.assertTrue(field2_1.isRequired());

        final DataStructure field2_2 = dataStructures.get(0).getDataStructures().get(1);
        Assert.assertEquals(DataType.DECIMAL, field2_2.getDataType());
        Assert.assertEquals("field2_2", field2_2.getField());
        Assert.assertEquals("field2_2", field2_2.getName());
        Assert.assertFalse(field2_2.isRequired());
    }

}