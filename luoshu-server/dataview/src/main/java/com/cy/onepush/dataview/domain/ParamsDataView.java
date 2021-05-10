package com.cy.onepush.dataview.domain;

import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.common.publishlanguage.version.Version;

public abstract class ParamsDataView extends DataView {

    public ParamsDataView(DataViewId id, String name) {
        super(id, name);
    }

    public ParamsDataView(DataViewId id, String name, Version version) {
        super(id, name, version);
    }

    protected abstract boolean validateParams();

    @Override
    protected boolean _checkParams() {
        return validateParams();
    }

}
