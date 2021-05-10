package com.cy.onepush.console.domain;

import com.cy.onepush.common.publishlanguage.console.ConsoleId;
import com.cy.onepush.common.framework.domain.AbstractAggregateRoot;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * the console to show {@link Log}s
 */
@Slf4j
public class Console extends AbstractAggregateRoot<String> {

    private final List<Log> logs;

    public Console(ConsoleId id) {
        super(id);
        this.logs = new ArrayList<>(256);
    }

    @Override
    public ConsoleId getId() {
        return ConsoleId.of(super.getId().getId());
    }

    public void addLog(String level, String content) {
        logs.add(Log.of(level, content));
        Console.log.info("{} {}", level, content);
    }

    public List<String> print() {
        // 输出
        final List<String> prints = this.logs.stream().map(Log::getLog).collect(Collectors.toList());
        // 清理
        this.logs.clear();

        return prints;
    }

}
