package com.cy.onepush.console.interfaces.impl;

import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.console.application.ConsoleApplicationService;
import com.cy.onepush.console.interfaces.ConsoleApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ConsoleApiImpl implements ConsoleApi {

    private final ConsoleApplicationService consoleApplicationService;

    @Override
    public Result<List<String>> logs(String consoleCode) {
        return Result.<List<String>>builder()
            .success(consoleApplicationService.getLogs(consoleCode))
            .build();
    }

}
