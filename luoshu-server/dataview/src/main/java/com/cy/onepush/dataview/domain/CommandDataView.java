package com.cy.onepush.dataview.domain;

import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.common.publishlanguage.version.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class CommandDataView extends DataView {

    private String command;

    public CommandDataView(DataViewId id, String name) {
        super(id, name);
    }

    public CommandDataView(DataViewId id, String name, Version version) {
        super(id, name, version);
    }

    @Override
    protected boolean _checkParams() {
        if (StringUtils.isBlank(getCommand())) {
            log.warn("the jdbc data view {} command is null", getId().getId());
            return false;
        }
        return true;
    }

    @Override
    public void setParams(Map<String, Object> params) {
        super.setParams(params);
        this.command = MapUtils.getString(params, "command");
    }

    @Override
    protected boolean _init() {
        return super._init();
    }

}
