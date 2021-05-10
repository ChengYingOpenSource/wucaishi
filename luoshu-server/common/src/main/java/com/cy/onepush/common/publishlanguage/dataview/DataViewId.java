package com.cy.onepush.common.publishlanguage.dataview;

import com.cy.onepush.common.framework.publishedlanguage.AggregateId;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class DataViewId extends AggregateId<String> {

    public static DataViewId of(String id) {
        return new DataViewId(id);
    }

    protected DataViewId(String id) {
        super(id);
    }

}
