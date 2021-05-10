package com.cy.onepush.common.utils;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.DefaultIdStrategy;
import io.protostuff.runtime.IdStrategy;
import io.protostuff.runtime.RuntimeSchema;

/**
 * serializer utils based on protostuff
 *
 * @author WhatAKitty
 * @date 2020-12-24
 * @since 0.1.0
 */
public class SerializeUtils {

    static final DefaultIdStrategy STRATEGY = new DefaultIdStrategy(IdStrategy.DEFAULT_FLAGS
        | IdStrategy.MORPH_COLLECTION_INTERFACES
        | IdStrategy.MORPH_MAP_INTERFACES
        | IdStrategy.MORPH_NON_FINAL_POJOS);

    public static <T> byte[] beanToBytes(T obj, Class<T> klass) {
        final Schema<T> schema = RuntimeSchema.getSchema(klass, STRATEGY);

        // Re-use (manage) this buffer to avoid allocating on every serialization
        final LinkedBuffer buffer = LinkedBuffer.allocate(512);

        // ser
        final byte[] protostuff;
        try {
            protostuff = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }

        return protostuff;
    }

    public static <T> T bytesToBean(byte[] protostuff, Class<T> klass) {
        final Schema<T> schema = RuntimeSchema.getSchema(klass, STRATEGY);

        T parsed = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(protostuff, parsed, schema);

        return parsed;
    }

    private SerializeUtils() {
    }

}
