package com.cy.onepush.config;

import com.cy.onepush.common.exception.DuplicateResourceException;
import com.cy.onepush.common.exception.IllegalParamsException;
import com.cy.onepush.common.exception.IllegalStateException;
import com.cy.onepush.common.exception.ResourceNotFoundException;
import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.common.framework.infrastructure.web.ResultCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class AppExceptionAdvice {

    private final ObjectMapper objectMapper;

    public AppExceptionAdvice(@Qualifier("exceptionObjectMapper") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result<Void> resolveMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("failed with method argument not valid exception", e);
        final List<String> msgs = e.getBindingResult().getAllErrors().stream()
            .map(ObjectError::getDefaultMessage)
            .collect(Collectors.toList());

        return Result.<Void>builder()
            .failed(ResultCode.BAD_REQUEST, writeValueAsString(msgs))
            .build();
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public Result<Void> resolveResourceNotFoundException(ResourceNotFoundException e) {
        log.error("failed with resource not found exception", e);
        return Result.<Void>builder()
            .failed(ResultCode.NOT_FOUND, e.getMessage())
            .build();
    }

    @ExceptionHandler({IllegalStateException.class})
    public Result<Void> resolveIllegalStateException(IllegalStateException e) {
        log.error("failed with illegal state exception", e);
        return Result.<Void>builder()
            .failed(ResultCode.BAD_REQUEST, e.getMessage())
            .build();
    }

    @ExceptionHandler({IllegalParamsException.class})
    public Result<Void> resolveIllegalStateException(IllegalParamsException e) {
        log.error("failed with illegal params exception", e);
        return Result.<Void>builder()
            .failed(ResultCode.BAD_REQUEST, e.getMessage())
            .build();
    }

    @ExceptionHandler({DuplicateResourceException.class})
    public Result<Void> resolveDuplicateException(DuplicateResourceException e) {
        log.error("failed with duplicate resource exception", e);
        return Result.<Void>builder()
            .failed(ResultCode.BAD_REQUEST, e.getMessage())
            .build();
    }

    @ExceptionHandler({RuntimeException.class})
    public Result<Void> resolveUnknownException(RuntimeException e) {
        log.error("failed with unknown exception", e);
        return Result.<Void>builder()
            .failed(ResultCode.ERROR, e.getMessage())
            .build();
    }

    private String writeValueAsString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return StringUtils.EMPTY;
        }
    }

    @Configuration
    public static class ExceptionAdviceConfiguration {

        @Bean("exceptionObjectMapper")
        public ObjectMapper exceptionObjectMapper() {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper;
        }

    }

}
