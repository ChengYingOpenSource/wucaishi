package com.cy.onepush.plugins.extension.datasource.es.bean;

import lombok.Data;

/**
 * 集群健康状态
 *
 * @author WhatAKitty
 * @date 2021年7月8日
 */
@Data
public class ClusterHealth {

    private String clusterName;
    private StatusEnum status;
    private Boolean timedOut;
    private Integer numberOfNodes;
    private Integer numberOfDataNodes;
    private Integer activeShards;
    private Integer relocatingShards;
    private Integer initialingShards;
    private Integer unassignedShards;
    private Integer delayedUnassignedShards;
    private Integer numberOfPendingStatus;
    private Integer numberOfInFlightFetch;
    private Integer taskMaxWaitingInQueueMillis;
    private Integer activeShardsPercentAsNumber;

}
