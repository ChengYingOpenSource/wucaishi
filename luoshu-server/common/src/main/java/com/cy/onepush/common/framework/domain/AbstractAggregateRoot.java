package com.cy.onepush.common.framework.domain;

import com.cy.onepush.common.framework.publishedlanguage.AggregateId;
import com.cy.onepush.common.utils.DomainEventUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;
import sun.misc.Unsafe;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * base aggregate root
 *
 * @author WhatAKitty
 * @date 2019/05/01
 * @description
 **/
@Data
public abstract class AbstractAggregateRoot<PK> implements Serializable {

    private static final Unsafe unsafe;

    private final AggregateId<PK> id;
    @EqualsAndHashCode.Exclude
    private AggregateStatus status = AggregateStatus.ACTIVE;

    public AbstractAggregateRoot(AggregateId<PK> id) {
        this.id = id.clone();
    }

    /**
     * publish event
     *
     * @param event event object
     */
    protected void publishEvent(ApplicationEvent event) {
        // TODO need to use another plan instead of injecting spring dependency
        DomainEventUtils.publishEvent(event);
    }

    /**
     * active this aggregate root
     */
    protected void active() {
        this.status = AggregateStatus.ACTIVE;
    }

    /**
     * invalid this aggregate root
     */
    protected void invalid() {
        this.status = AggregateStatus.INVALID;
    }

    /**
     * check the aggregate root status, if {invalid} throw {@link IllegalAggregateStatus}
     */
    public void checkActive() {
        if (!AggregateStatus.ACTIVE.equals(this.status)) {
            throw new IllegalAggregateStatus();
        }
    }

    static {
        try {
            final Field unsafeField = Unsafe.class.getDeclaredFields()[0];
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(null);
        } catch (IllegalAccessException e) {
            throw new Error(e);
        }

    }

}
