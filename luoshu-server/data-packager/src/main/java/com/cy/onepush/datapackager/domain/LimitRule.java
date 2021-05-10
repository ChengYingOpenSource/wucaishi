package com.cy.onepush.datapackager.domain;

import com.cy.onepush.common.framework.domain.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

/**
 * limit rule
 *
 * @author WhatAKitty
 * @date 2021-2-7
 * @since 0.0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LimitRule extends AbstractEntity<String> {

    public static LimitRule of(String pk, String expression) {
        return new LimitRule(pk, expression);
    }

    private final Expression expression;

    private LimitRule(String pk, String expression) {
        this.setId(pk);
        this.expression = new SpelExpressionParser().parseExpression(expression);
    }

    public boolean isMatch(LimitResource limitResource) {
        if (limitResource == null) {
            return Boolean.FALSE;
        }

        final Boolean result = expression.getValue(limitResource, Boolean.class);
        return result == null ? Boolean.FALSE : result;
    }

}
