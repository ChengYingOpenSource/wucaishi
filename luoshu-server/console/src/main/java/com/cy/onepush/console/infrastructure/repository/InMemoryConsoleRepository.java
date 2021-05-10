package com.cy.onepush.console.infrastructure.repository;

import com.cy.onepush.common.publishlanguage.console.ConsoleId;
import com.cy.onepush.console.domain.Console;
import com.cy.onepush.console.domain.ConsoleRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryConsoleRepository implements ConsoleRepository {

    private final Map<ConsoleId, Console> CONSOLE_HOLDER = new ConcurrentHashMap<>(16);

    @Override
    public Console getOrCreate(ConsoleId consoleId) {
        return CONSOLE_HOLDER.compute(consoleId, (k, old) -> {
            if (old != null) {
                return old;
            }

            return new Console(k);
        });
    }

}
