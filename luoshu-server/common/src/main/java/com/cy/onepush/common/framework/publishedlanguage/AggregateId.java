package com.cy.onepush.common.framework.publishedlanguage;

import java.io.Serializable;
import lombok.Data;

/**
 * aggregate root id
 *
 * @author WhatAKitty
 * @date 2019/05/02
 * @description
 **/
@Data
public class AggregateId<T> implements Serializable, Cloneable {

    public static <T> AggregateId<T> of(T id) {
        return new AggregateId<>(id);
    }

    private final T id;

    protected AggregateId(T id) {
        this.id = id;
    }

    @Override
    public AggregateId<T> clone() {
        return AggregateId.of(id);
    }

}
