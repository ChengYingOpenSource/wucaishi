package com.cy.onepush.pipeline.domain;

import com.cy.onepush.common.publishlanguage.pipeline.PipelineId;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

public class PipelineTest {

    private final ObjectMapper MAPPER = new ObjectMapper();
    {
        MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @Test
    public void test_serialization() {
        Pipeline pipeline = new Pipeline(PipelineId.of("test"));

        final String result;
        try {
            result = MAPPER.writeValueAsString(pipeline);
            System.out.println(result);
        } catch (JsonProcessingException e) {
            Assert.fail();
            return;
        }

        final Pipeline pipeline1;
        try {
            pipeline1 = MAPPER.readValue(result, Pipeline.class);
        } catch (JsonProcessingException e) {
            Assert.fail();
            return;
        }

        Assert.assertEquals(pipeline, pipeline1);
    }

}