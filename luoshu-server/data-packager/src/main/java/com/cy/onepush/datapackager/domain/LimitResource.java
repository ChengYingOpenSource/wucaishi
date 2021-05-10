package com.cy.onepush.datapackager.domain;

import com.cy.onepush.common.framework.domain.AbstractEntity;
import com.cy.onepush.common.utils.ByteUnit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.concurrent.TimeUnit;

@Data
@EqualsAndHashCode(callSuper = true)
public class LimitResource extends AbstractEntity<Long> {

    private long threadId;

    private long checks = 0L;

    private long baseMemoryUsage = 0L;
    private long currentMemoryUsage = 0L;
    private long maximumMemoryUsage = 0L;
    private long averageMemoryUsage = 0L;

    private long baseCpuUsage = 0L;
    private long currentCpuUsage = 0L;
    private long maximumCpuUsage = 0L;
    private long averageCpuUsage = 0L;

    public LimitResource(long threadId) {
        this.setId(this.threadId = threadId);
    }

    public void calcMaximumMemoryUsage() {
        if (currentMemoryUsage > this.maximumMemoryUsage) {
            this.maximumMemoryUsage = currentMemoryUsage;
        }
    }

    public void calcMaximumCpuUsage() {
        if (currentCpuUsage > this.maximumCpuUsage) {
            this.maximumCpuUsage = currentCpuUsage;
        }
    }

    public void calcAverageUsage() {
        if (checks == 0L) {
            averageMemoryUsage = currentMemoryUsage;
            averageCpuUsage = currentCpuUsage;
            return;
        }

        averageMemoryUsage = (averageMemoryUsage * checks + currentMemoryUsage) / checks;
        averageCpuUsage = (averageCpuUsage * checks + currentCpuUsage) / checks;
    }

    public long incAndGetCheckCount() {
        return ++checks;
    }

    public String statistics() {
        return String.format("memory statistics: current usage %f, maximum usage %f, average usage %f\r\n" +
            "cpu statistics: current usage %d, maximum usage %d, average usage %d",
            ByteUnit.BYTE.toMiB(currentMemoryUsage), ByteUnit.BYTE.toMiB(maximumMemoryUsage), ByteUnit.BYTE.toMiB(averageMemoryUsage),
            TimeUnit.MILLISECONDS.toSeconds(currentCpuUsage), TimeUnit.MILLISECONDS.toSeconds(maximumMemoryUsage), TimeUnit.MILLISECONDS.toSeconds(averageCpuUsage));
    }

}
