package com.cy.onepush.common.utils;

import org.junit.Assert;
import org.junit.Test;

public class SerializeUtilsTest {

    @Test
    public void test_ser() {
        final TestClazz testClazz = new TestClazz(1);

        final byte[] bytes = SerializeUtils.beanToBytes(testClazz, TestClazz.class);

        Assert.assertNotNull(bytes);
    }

    @Test
    public void test_des() {
        final TestClazz testClazz = new TestClazz(1);

        final byte[] bytes = SerializeUtils.beanToBytes(testClazz, TestClazz.class);

        final TestClazz testClazz1 = SerializeUtils.bytesToBean(bytes, TestClazz.class);

        Assert.assertEquals(testClazz.a, testClazz1.a);
        Assert.assertEquals(testClazz.b, testClazz1.b);
    }

    public static class TestClazz {

        private final int a;
        private final int b;

        public TestClazz(int a) {
            this.a = a;
            this.b = 2;
        }

    }

}