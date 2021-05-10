package com.cy.onepush.pipeline.domain.event;

import com.cy.onepush.common.publishlanguage.pipeline.PipelineId;
import com.cy.onepush.pipeline.domain.PipelineStatus;
import org.springframework.context.ApplicationEvent;

public class PipelineDoneEvent extends ApplicationEvent {

    private final PipelineStatus pipelineStatus;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param pipelineId the object on which the event initially occurred or with
     *                   which the event is associated (never {@code null})
     */
    public PipelineDoneEvent(PipelineId pipelineId, PipelineStatus pipelineStatus) {
        super(pipelineId);
        this.pipelineStatus = pipelineStatus;
    }

    @Override
    public PipelineId getSource() {
        return (PipelineId) super.getSource();
    }

    public PipelineStatus getPipelineStatus() {
        return pipelineStatus;
    }
}
