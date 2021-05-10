package com.cy.onepush.common.utils;

import org.springframework.context.ApplicationEvent;

public class DomainEventUtils {

    public static void publishEvent(ApplicationEvent applicationEvent) {
        SpringUtils.getApplicationContext().publishEvent(applicationEvent);
    }

    private DomainEventUtils() {}

}
