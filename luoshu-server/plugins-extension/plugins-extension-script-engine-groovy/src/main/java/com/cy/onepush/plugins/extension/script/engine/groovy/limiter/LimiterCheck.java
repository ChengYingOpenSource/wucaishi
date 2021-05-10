package com.cy.onepush.plugins.extension.script.engine.groovy.limiter;

import com.cy.onepush.common.exception.LimitExceedException;
import com.cy.onepush.datapackager.domain.LimitResource;
import com.sun.management.ThreadMXBean;
import groovy.lang.Binding;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.function.Consumer;

@Slf4j
public class LimiterCheck {

    static final String CHECKER_FIELD = "$$limiterCheck";

    /* ThreadMXBean used to enforce memory quota */
    private final ThreadMXBean threadMXBean;
    /* Thread ID whose memory usage will be checked */
    private long threadId;

    private Consumer<LimitResource> limiter;

    /* Checking enabled */
    private boolean enabled = true;

    /* Groovy script binding */
    private Binding scriptBinding;

    private LimitResource limitResource;

    /**
     * Creates a MemoryCheck that uses the given ThreadMXBean to watch a given thread's memory consumption
     *
     * @param threadMXBean {@link ThreadMXBean} that will be used to measure thread memory allocation
     */
    public LimiterCheck(@NotNull ThreadMXBean threadMXBean) {
        this.threadMXBean = threadMXBean;
    }

    public void setLimiter(Consumer<LimitResource> limiter) {
        this.limiter = limiter;
    }

    /**
     * @return the watched thread's ID
     */
    public long getThreadId() {
        return threadId;
    }

    /**
     * Define the thread's id
     *
     * @param threadId thread id whose memory comsumption will be checked
     */
    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    private void updateStats() {
        this.limitResource.calcMaximumMemoryUsage();
        this.limitResource.calcMaximumCpuUsage();

        this.limitResource.calcAverageUsage();

        this.limitResource.incAndGetCheckCount();
    }

    private void recordBaseUsage() {
        this.limitResource.setBaseMemoryUsage(this.threadMXBean.getThreadAllocatedBytes(this.threadId));
        this.limitResource.setBaseCpuUsage(this.threadMXBean.getThreadCpuTime(this.threadId));
    }

    /**
     * Check the thread's memory usage, executes infringement handler if defined.
     */
    void check() {
        if (threadId == 0L) {
            throw new IllegalStateException("Invalid thread id for expression quota check");
        }

        final long currentMemoryUsage = threadMXBean.getThreadAllocatedBytes(threadId) - this.limitResource.getBaseMemoryUsage();
        this.limitResource.setCurrentMemoryUsage(currentMemoryUsage);
        final long currentCpuUsage = threadMXBean.getThreadCpuTime(threadId) - this.limitResource.getBaseCpuUsage();
        this.limitResource.setCurrentCpuUsage(currentCpuUsage);

        updateStats();

        // not enable limiter or limiter is null
        if (!enabled || limiter == null) {
            return;
        }

        try {
            log.info(this.limitResource.statistics());
            limiter.accept(limitResource);
        } catch (LimitExceedException e) {
            throw e;
        } catch (Exception e) {
            // unknown exception
            log.error("failed to calculate the limit for this resource due to", e);
        }
    }

    public void setScriptBinding(Binding scriptBinding) {
        this.scriptBinding = scriptBinding;
    }

    public Binding getScriptBinding() {
        return this.scriptBinding;
    }

    /**
     * This is a convenience method to set this MemoryQuotaChecker's thrad id to the current threads id and base memory
     * usage to the current thread's memory usage
     */
    void init() {
        threadId = Thread.currentThread().getId();
        limitResource = new LimitResource(threadId);
        recordBaseUsage();
    }

}
