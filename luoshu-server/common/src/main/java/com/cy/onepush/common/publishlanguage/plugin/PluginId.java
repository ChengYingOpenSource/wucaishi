package com.cy.onepush.common.publishlanguage.plugin;

import com.cy.onepush.common.framework.publishedlanguage.AggregateId;

public class PluginId extends AggregateId<String> {

    public static PluginId of(String id) {
        return new PluginId(id);
    }

    protected PluginId(String id) {
        super(id);
    }

    @Override
    public String getId() {
        return super.getId();
    }

}
