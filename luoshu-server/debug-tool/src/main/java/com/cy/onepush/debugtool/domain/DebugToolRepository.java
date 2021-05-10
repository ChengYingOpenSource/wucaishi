package com.cy.onepush.debugtool.domain;

import com.cy.onepush.common.publishlanguage.debugtool.DebugToolId;
import com.cy.onepush.console.domain.Console;

public interface DebugToolRepository {

    DebugTool getOrCreate(DebugToolId debugToolId, Console console);

}
