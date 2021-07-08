package com.cy.onepush.plugins.extension.datasource.es.bean;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 状态枚举
 *
 * @author WhatAKitty
 * @date 2021年7月8日
 */
public enum StatusEnum {

    /**
     * 健康
     */
    GREEN("green"),
    /**
     * 存在问题
     */
    YELLOW("yellow"),
    /**
     * 严重问题
     */
    RED("red");

    StatusEnum(String value) {
        this.value = value;
    }

    /**
     * 值
     */
    @Getter
    private final String value;

    @JsonCreator
    public static StatusEnum fromString(String string) {
        return Arrays.stream(StatusEnum.values())
            .filter(item -> StringUtils.equals(item.getValue(), string))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public String toString() {
        return value.toUpperCase();
    }

}
