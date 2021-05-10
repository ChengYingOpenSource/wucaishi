package com.cy.onepush.plugins.utils;

import java.lang.annotation.Annotation;
import java.util.Optional;

public class BeanUtils {

    public static <T extends Annotation> Optional<T> getAnnotation(Class<?> targetClass, Class<T> annotationClass) {
        if (!targetClass.isAnnotationPresent(annotationClass)) {
            return Optional.empty();
        }

        return Optional.of(targetClass.getAnnotation(annotationClass));
    }



}
