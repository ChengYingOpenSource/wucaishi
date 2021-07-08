package com.cy.onepush.plugins.extension.datasource.es;

import com.cy.onepush.common.utils.NamedParameterUtils;
import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;

public class NamedParameterUtilsTest {

    @Test
    public void test_named() {
        final String str = NamedParameterUtils.replace("hello :name", ImmutableMap.of(
            "name", "kitty"
        ));
        Assert.assertEquals("hello kitty", str);
    }

    @Test
    public void test_more_named() {
        final String str = NamedParameterUtils.replace("hello :name, nice day, :bye.", ImmutableMap.of(
            "name", "kitty",
            "bye", "bye"
        ));
        Assert.assertEquals("hello kitty, nice day, bye.", str);
    }


}
