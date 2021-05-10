package com.cy.onepush.pipeline.interfaces;

import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.pipeline.interfaces.params.PipelineCreateGatewayParam;
import com.cy.onepush.pipeline.interfaces.vo.PipelineVO;
import org.springframework.web.bind.annotation.*;

/**
 * pipeline api
 *
 * @author WhatAKitty
 * @date 2020-12-17
 * @since 0.1.0
 */
@RequestMapping(path = "/api/v1/pipelines")
public interface PipelineApi {

    /**
     * @api {GET} /api/v1/pipelines/byGateway/{gatewayCode}/{version} 获取Pipeline
     * @apiGroup PipelineApi
     * @apiName 获取Pipeline
     * @apiDescription 获取Pipeline
     * @apiVersion 0.1.0
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Object} data 数据
     * @apiSuccess (data) {Object} gateway 接口参数
     * @apiSuccess (data) {Object} dataPackager 数据组装参数
     * @apiSuccess (data) {Array} dataViewMappings 数据视图参数
     * @apiSuccess (data) {String} version 版本
     * @apiSuccess (gateway) {String} gatewayCode 接口编码
     * @apiSuccess (dataPackager) {String} dataPackagerCode 数据组装编码
     * @apiSuccess (dataPackager) {String} scriptType 脚本类型（请求脚本类型接口）
     * @apiSuccess (dataPackager) {String} scriptContent 脚本内容
     * @apiSuccess (dataPackager) {Object} requestDataStructure 脚本请求数据结构
     * @apiSuccess (dataPackager) {Object} responseDataStructure 脚本响应数据结构
     * @apiSuccess (dataViewMappings) {String} dataViewCode 视图编码
     * @apiSuccess (dataViewMappings) {String} dataViewType 数据源编码
     * @apiSuccess (dataViewMappings) {String} dataSourceCode 数据源编码
     * @apiSuccess (dataViewMappings) {Object} dataViewParams 视图参数
     * @apiSuccess (dataViewMappings) {Object} requestDataStructure 视图请求数据结构
     * @apiSuccess (dataViewMappings) {Object} responseDataStructure 视图响应数据结构
     * @apiSuccess (dataSource) {Object} dataSourceCode 数据源编码
     * @apiSuccess (dataSource) {Object} dataSourceName 数据源名称
     * @apiSuccess (dataSource) {String} dataSourceType 数据源类型（请求数据源类型接口）
     * @apiSuccess (dataSource) {Object} dataSourceProperties 数据源配置
     * @apiSuccessExample {json} Success-Response:
     * {
     * "version": "1.0.0",
     * "gatewayParam": {
     * "gatewayCode": "test_gateway"
     * },
     * "dataPackagerParam": {
     * "dataPackagerCode": "test_packager",
     * "scriptType": "groovy",
     * "scriptContent": "return dataViewers.get(\"test_view\");",
     * "requestDataStructure": [{
     * "dataType": "INT",
     * "name": "a",
     * "field": "a",
     * "required": true
     * }],
     * "responseDataStructure": [{
     * "dataType": "INT",
     * "name": "b",
     * "field": "b",
     * "required": true
     * }]
     * },
     * "dataViewMappingParams": [{
     * "dataViewCode": "test_view",
     * "dataViewType": "JDBC",
     * "dataViewParams": {
     * "command": ""
     * },
     * "dataSourceCode": "rds_code",
     * "requestDataStructure": [{
     * "dataType": "INT",
     * "name": "a",
     * "field": "a",
     * "required": true
     * }],
     * "responseDataStructure": [{
     * "dataType": "INT",
     * "name": "b",
     * "field": "b",
     * "required": true
     * }]
     * }, {
     * "dataViewCode": "test_view_2",
     * "dataViewParams": {
     * "contextUrl": "/a/b/c",
     * "method": "GET"
     * },
     * "dataSourceCode": "http_code",
     * "requestDataStructure": [{
     * "dataType": "INT",
     * "name": "a",
     * "field": "a",
     * "required": true
     * }],
     * "responseDataStructure": [{
     * "dataType": "INT",
     * "name": "b",
     * "field": "b",
     * "required": true
     * }]
     * }
     * ]
     * }
     */
    @GetMapping(path = "/byGateway/{gatewayCode}/{version}")
    Result<PipelineVO> get(@PathVariable("gatewayCode") String gatewayCode, @PathVariable("version") String version);

}
