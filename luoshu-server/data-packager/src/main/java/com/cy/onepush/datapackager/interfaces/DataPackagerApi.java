package com.cy.onepush.datapackager.interfaces;

import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.datapackager.interfaces.params.DebugDataPackagerParams;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * data packager api
 *
 * @author WhatAKitty
 * @date 2020-12-16
 * @since 0.1.0
 */
@RestController
@RequestMapping(path = "/api/v1/datapackagers")
public interface DataPackagerApi {

    /**
     * @api {GET} /api/v1/datapackagers/scriptTypes 可执行脚本类型
     * @apiGroup DataPackagerApi
     * @apiName 可执行脚本类型
     * @apiDescription 可执行脚本类型
     * @apiVersion 0.1.0
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Array} data 数据格式类型列表
     * @apiSuccessExample {json} Success-Response:
     * {
     * "code": 0,
     * "success": true,
     * "msg": "",
     * "data": ["Groovy", "ECMAScript"]
     * }
     */
    @GetMapping(path = "/scriptTypes")
    Result<List<String>> scriptTypes();

    /**
     * @api {POST} /api/v1/datapackagers/debug 数据组装DEBUG
     * @apiGroup DataPackagerApi
     * @apiName 数据组装DEBUG
     * @apiDescription 数据组装DEBUG
     * @apiVersion 0.1.0
     * @apiParam {Object} dataPackagerParam 数据组装参数
     * @apiParam {Array} dataViewMappingParams 数据视图参数
     * @apiParam {String} version 版本
     * @apiParam {Object} executionParams 调试参数
     * @apiParam (gatewayParam) {String} gatewayCode 接口编码
     * @apiParam (dataPackagerParam) {String} dataPackagerCode 数据组装编码
     * @apiParam (dataPackagerParam) {String} scriptType 脚本类型（请求脚本类型接口）
     * @apiParam (dataPackagerParam) {String} scriptContent 脚本内容
     * @apiParam (dataPackagerParam) {Object} requestDataStructure 脚本请求数据结构
     * @apiParam (dataPackagerParam) {Object} responseDataStructure 脚本响应数据结构
     * @apiParam (dataViewMappingParams) {String} dataViewCode 视图编码
     * @apiParam (dataViewMappingParams) {Object} dataSourceCode 数据源编码
     * @apiParam (dataViewMappingParams) {Object} dataViewParams 视图参数
     * @apiParam (dataViewMappingParams) {Object} requestDataStructure 视图请求数据结构
     * @apiParam (dataViewMappingParams) {Object} responseDataStructure 视图响应数据结构
     * @apiParam (dataSourceParam) {Object} dataSourceCode 数据源编码
     * @apiParam (dataSourceParam) {Object} dataSourceName 数据源名称
     * @apiParam (dataSourceParam) {String} dataSourceType 数据源类型（请求数据源类型接口）
     * @apiParam (dataSourceParam) {Object} dataSourceProperties 数据源配置
     * @apiParam (executionParams) {Object} params 执行参数
     * @apiParamExample {json} 请求参数示例
     * {
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
     * "dataViewParams": {
     * "command": ""
     * },
     * "dataSourceCode": "test_datasource",
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
     * "dataSourceCode": "test_datasource",
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
     * ],
     * "executionParams": {
     * "params": {
     * "a": 1
     * }
     * }
     * }
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {String} data 控制台编码
     * @apiSuccessExample {json} Success-Response:
     * {"code":0,"success":true,"msg":"","data": "test_console_code" }
     */
    @PostMapping(path = "/debug")
    Result<String> debug(@Validated @RequestBody DebugDataPackagerParams params);

}
