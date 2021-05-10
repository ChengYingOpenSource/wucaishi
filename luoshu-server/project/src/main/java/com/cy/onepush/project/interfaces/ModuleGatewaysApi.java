package com.cy.onepush.project.interfaces;

import com.cy.onepush.common.framework.infrastructure.web.PaginationResult;
import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.pipeline.interfaces.params.PipelineCreateGatewayParam;
import com.cy.onepush.pipeline.interfaces.params.groups.PipelineCreateGatewayGroup;
import com.cy.onepush.pipeline.interfaces.params.groups.PipelineUpdateGatewayGroup;
import com.cy.onepush.project.interfaces.params.SearchModuleGatewayParams;
import com.cy.onepush.project.interfaces.vo.ModuleGatewayVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.groups.Default;

@RequestMapping(path = "/api/v1/modules/{moduleCode}/gateways")
public interface ModuleGatewaysApi {

    /**
     * @api {GET} /api/v1/modules/{module}/gateways 获取子模块接口列表
     * @apiGroup ModuleGatewaysApi
     * @apiName 获取子模块接口列表
     * @apiDescription 获取子模块接口列表
     * @apiVersion 0.1.0
     * @apiParam {Number} pageSize 获取页大小
     * @apiParam {Number} pageNum 当前页
     * @apiParam {String} [app] 子模块编码
     * @apiParam {String} [name] 名称（模糊搜索）
     * @apiParam {Number=0,1} [status] 接口状态（0：待上线；1：已上线）
     * @apiParam {String} version 版本
     * @apiParamExample {json} 请求参数示例
     * {
     * "app": "test_module",
     * "status": 1,
     * "name": "test",
     * "pageSize": 10,
     * "pageNum": 1,
     * "version": "1.0.0"
     * }
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Object} data 数据
     * @apiSuccess (data) {Array} list 数据
     * @apiSuccess (data) {Object} pagination 数据
     * @apiSuccess (list) {String} project 项目编码
     * @apiSuccess (list) {String} app 子模块编码
     * @apiSuccess (list) {String} apiKey 接口编码
     * @apiSuccess (list) {String} version 版本
     * @apiSuccess (list) {String} description 接口描述
     * @apiSuccess (list) {String} createTime 创建时间
     * @apiSuccess (list) {String} updateTime 修改时间
     * @apiSuccess (list) {Array} dataViewers 数据视图列表
     * @apiSuccess (dataViewers) {String} dataViewCode 数据视图编码
     * @apiSuccess (dataViewers) {String} dataViewType 数据视图类型
     * @apiSuccess (dataViewers) {String} dataSourceName 数据源名称
     * @apiSuccess (pagination) {Number} pageNum 页码
     * @apiSuccess (pagination) {Number} pageSize 页大小
     * @apiSuccess (pagination) {Number} total 总大小
     * @apiSuccessExample {json} Success-Response:
     * {
     * "code": 0,
     * "success": true,
     * "msg": "",
     * "data": {
     * "list": [
     * {
     * "project": "test_project",
     * "app": "test",
     * "apiKey": "test_module_api_key",
     * "createTime": "2020-12-01 11:22:30",
     * "updateTime": "2020-12-01 11:22:30",
     * "version": "1.0.0",
     * "status": 1,
     * "description": "",
     * "dataViewers": [{
     * "dataViewCode": "xxx_view_code",
     * "dataViewType": "JDBC",
     * "dataSourceName": "XXX datasource"
     * }]
     * }
     * ],
     * "pagination": {
     * "pageSize": 10,
     * "pageNum": 1,
     * "total": 3
     * }
     * }
     * }
     */
    @GetMapping
    PaginationResult<ModuleGatewayVO> list(@PathVariable("moduleCode") String moduleCode, @Validated SearchModuleGatewayParams params);

    /**
     * @api {POST} /api/v1/modules/{module}/gateways/pipelined 创建Pipeline式样的接口
     * @apiGroup ModuleGatewaysApi
     * @apiName 创建Pipeline
     * @apiDescription 创建Pipeline
     * @apiVersion 0.1.0
     * @apiParam {Object} gatewayParam 接口参数
     * @apiParam {Object} dataPackagerParam 数据组装参数
     * @apiParam {Array} dataViewMappingParams 数据视图参数
     * @apiParam {String} version 版本
     * @apiParam (gatewayParam) {String} gatewayCode 接口编码
     * @apiParam (dataPackagerParam) {String} dataPackagerCode 数据组装编码
     * @apiParam (dataPackagerParam) {String} scriptType 脚本类型（请求脚本类型接口）
     * @apiParam (dataPackagerParam) {String} scriptContent 脚本内容
     * @apiParam (dataPackagerParam) {Object} requestDataStructure 脚本请求数据结构
     * @apiParam (dataPackagerParam) {Object} responseDataStructure 脚本响应数据结构
     * @apiParam (dataViewMappingParams) {String} dataViewCode 视图编码
     * @apiParam (dataViewMappingParams) {String} dataSourceCode 数据源编码
     * @apiParam (dataViewMappingParams) {Object} dataViewParams 视图参数
     * @apiParam (dataViewMappingParams) {Object} requestDataStructure 视图请求数据结构
     * @apiParam (dataViewMappingParams) {Object} responseDataStructure 视图响应数据结构
     * @apiParamExample {json} 请求参数示例:
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
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Boolean} data 创建成功的接口编码
     * @apiSuccessExample {json} Success-Response:
     * {"code":0,"success":true,"msg":"","data": "interface_code" }
     */
    @PostMapping(path = "/pipelined")
    Result<String> createGateway(@PathVariable("moduleCode") String moduleCode,
                                 @Validated({PipelineCreateGatewayGroup.class, Default.class}) @RequestBody PipelineCreateGatewayParam param);

    /**
     * @api {PUT} /api/v1/modules/{module}/gateways/{gatewayCode}/{version}/pipelined 修改Pipeline
     * @apiGroup PipelineApi
     * @apiName 修改Pipeline
     * @apiDescription 修改某个网关对应的Pipeline
     * @apiVersion 0.1.0
     * @apiParam {Object} gatewayParam 接口参数
     * @apiParam {Object} dataPackagerParam 数据组装参数
     * @apiParam {Array} dataViewMappingParams 数据视图参数
     * @apiParam {String} version 版本
     * @apiParam (gatewayParam) {String} gatewayCode 接口编码
     * @apiParam (dataPackagerParam) {String} dataPackagerCode 数据组装编码
     * @apiParam (dataPackagerParam) {String} scriptType 脚本类型（请求脚本类型接口）
     * @apiParam (dataPackagerParam) {String} scriptContent 脚本内容
     * @apiParam (dataPackagerParam) {Object} requestDataStructure 脚本请求数据结构
     * @apiParam (dataPackagerParam) {Object} responseDataStructure 脚本响应数据结构
     * @apiParam (dataViewMappingParams) {String} dataViewCode 视图编码
     * @apiParam (dataViewMappingParams) {String} dataSourceCode 数据源编码
     * @apiParam (dataViewMappingParams) {Object} dataViewParams 视图参数
     * @apiParam (dataViewMappingParams) {Object} requestDataStructure 视图请求数据结构
     * @apiParam (dataViewMappingParams) {Object} responseDataStructure 视图响应数据结构
     * @apiParamExample {json} 请求参数示例:
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
     * "dataViewParams": {
     * "command": ""
     * },
     * "dataSourceCode": "rds_code"
     * },
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
     * "dataSourceCode": "http_code"
     * },
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
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Boolean} data 创建成功的接口编码
     * @apiSuccessExample {json} Success-Response:
     * {"code":0,"success":true,"msg":"","data": "interface_code" }
     */
    @PutMapping(path = "/{gatewayCode}/{version}/pipelined")
    Result<?> updateGateway(@PathVariable("moduleCode") String moduleCode,
                            @PathVariable("gatewayCode") String gatewayCode,
                            @PathVariable("version") String version,
                            @Validated({PipelineUpdateGatewayGroup.class, Default.class}) @RequestBody PipelineCreateGatewayParam param);


}
