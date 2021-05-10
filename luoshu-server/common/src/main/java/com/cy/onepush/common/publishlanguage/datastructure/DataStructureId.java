package com.cy.onepush.common.publishlanguage.datastructure;

import com.cy.onepush.common.framework.publishedlanguage.AggregateId;

public class DataStructureId extends AggregateId<String> {

    public static DataStructureId of(String id) {
        return new DataStructureId(id);
    }

    protected DataStructureId(String id) {
        super(id);
    }
}
