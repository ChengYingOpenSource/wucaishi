package com.cy.onepush.console.domain;

import com.cy.onepush.common.framework.domain.AbstractEntity;

/**
 * the log container to hold the log's content
 */
public class Log extends AbstractEntity<String> {

    public static Log of(String level, String content) {
        return new Log(level, content);
    }

    private final String level;
    private final String content;

    private Log(String level, String content) {
        this.level = level;
        this.content = content;
    }

    public String getLog() {
        return String.format("[%s] %s", level, content);
    }

}
