package com.cy.onepush.debugtool.application.listener;

import com.cy.onepush.common.publishlanguage.debugtool.DebugToolId;
import com.cy.onepush.console.application.ConsoleApplicationService;
import com.cy.onepush.console.domain.Console;
import com.cy.onepush.debugtool.domain.DebugTool;
import com.cy.onepush.debugtool.domain.DebugToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DebugToolApplicationService {

    private final ConsoleApplicationService consoleApplicationService;
    private final DebugToolRepository debugToolRepository;

    public DebugTool getOrCreate(String targetId) {
        final Console console = consoleApplicationService.getOrCreate(targetId);

        return debugToolRepository.getOrCreate(DebugToolId.of(targetId), console);
    }

}
