package com.cy.onepush.common.framework.event;

import org.springframework.context.ApplicationEvent;

/**
 * domain event
 *
 * @author WhatAKitty
 * @date 2019/05/02
 * @description
 **/
public abstract class DomainEvent extends ApplicationEvent {

    /**
     * Create a new Domain Event.
     *
     * @param source    the object on which the event initially occurred (never {@code null})
     */
    public DomainEvent(Object source) {
        super(source);
    }

}
