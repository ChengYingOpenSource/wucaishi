package com.cy.onepush.common.publishlanguage.pipeline;

import com.cy.onepush.common.framework.publishedlanguage.AggregateId;

public class PipelineId extends AggregateId<String> {

    public static PipelineId of(String id) {
        return new PipelineId(id);
    }

    protected PipelineId(String id) {
        super(id);
    }

}
