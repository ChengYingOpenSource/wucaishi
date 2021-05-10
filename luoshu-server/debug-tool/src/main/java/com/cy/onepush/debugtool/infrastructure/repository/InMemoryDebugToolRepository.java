package com.cy.onepush.debugtool.infrastructure.repository;

import com.cy.onepush.common.publishlanguage.debugtool.DebugToolId;
import com.cy.onepush.console.domain.Console;
import com.cy.onepush.debugtool.domain.DebugTool;
import com.cy.onepush.debugtool.domain.DebugToolRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * in memory debug tool repository
 *
 * @author WhatAKitty
 * @date 2020-12-13
 * @since 0.1.0
 */
@Component
public class InMemoryDebugToolRepository implements DebugToolRepository {

    private final Map<DebugToolId, DebugTool> DEBUG_TOOL_HOLDER = new ConcurrentHashMap<>();

    @Override
    public DebugTool getOrCreate(DebugToolId debugToolId, Console console) {
        return DEBUG_TOOL_HOLDER.compute(debugToolId, (k, old) -> {
            if (old != null) {
                return old;
            }

            return new DebugTool(k, console);
        });
    }

}
