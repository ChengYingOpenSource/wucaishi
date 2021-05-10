package com.cy.onepush.common.publishlanguage.project;

import com.cy.onepush.common.framework.publishedlanguage.AggregateId;

public class ProjectId extends AggregateId<String> {

    public static ProjectId of(String id) {
        return new ProjectId(id);
    }

    protected ProjectId(String id) {
        super(id);
    }
}
