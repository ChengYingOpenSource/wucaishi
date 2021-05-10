package com.cy.onepush.console.domain;

import com.cy.onepush.common.publishlanguage.console.ConsoleId;

public interface ConsoleRepository {

    Console getOrCreate(ConsoleId consoleId);

}
