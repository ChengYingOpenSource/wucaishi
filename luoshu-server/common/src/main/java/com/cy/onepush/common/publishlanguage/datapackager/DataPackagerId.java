package com.cy.onepush.common.publishlanguage.datapackager;

import com.cy.onepush.common.framework.publishedlanguage.AggregateId;

public class DataPackagerId extends AggregateId<String> {

    public static DataPackagerId of(String id) {
        return new DataPackagerId(id);
    }

    protected DataPackagerId(String id) {
        super(id);
    }
}
