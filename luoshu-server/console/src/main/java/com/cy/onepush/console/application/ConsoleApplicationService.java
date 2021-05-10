package com.cy.onepush.console.application;

import com.cy.onepush.common.publishlanguage.console.ConsoleId;
import com.cy.onepush.console.domain.Console;
import com.cy.onepush.console.domain.ConsoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsoleApplicationService {

    private final ConsoleRepository consoleRepository;

    public Console getOrCreate(String consoleId) {
        return consoleRepository.getOrCreate(ConsoleId.of(consoleId));
    }

    public List<String> getLogs(String consoleId) {
        final Console console = getOrCreate(consoleId);

        return console.print();
    }

}
