package com.cy.onepush.pipeline.domain;

import com.cy.onepush.common.exception.ResourceNotFoundException;

import java.util.Arrays;

/**
 * pipeline status
 * <p>
 * started
 * finished
 * failed
 */
public enum PipelineStatus {

    STARTED(0),
    FINISHED(9),
    FAILED(-1);

    public static PipelineStatus of(int value) {
        return Arrays.stream(PipelineStatus.values())
            .filter(pipelineStatus -> pipelineStatus.getValue() == value)
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("the pipeline status %d not supported", value));
    }

    private final int value;

    PipelineStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

}
