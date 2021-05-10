package com.cy.onepush.dataview.interfaces;

import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.dataview.interfaces.params.DebugDataViewParams;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(path = "/api/v1/dataviewes")
public interface DataViewApi {

    /**
     * @api {GET} /api/v1/dataviewes/types 数据视图类型
     * @apiGroup DataViewApi
     * @apiName 数据视图类型
     * @apiDescription 数据视图类型，诸如JDBCView、HTTPView等
     * @apiVersion 0.1.0
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Array} data 数据视图类型列表
     * @apiSuccessExample {json} Success-Response:
     * {
     * "code": 0,
     * "success": true,
     * "msg": "",
     * "data": ["JDBC", "HTTP"]
     * }
     */
    @GetMapping(path = "/types")
    Result<List<String>> types();

    /**
     * @api {POST} /api/v1/dataviewes/debug 视图DEBUG
     * @apiGroup DataViewApi
     * @apiName 视图DEBUG
     * @apiDescription 视图DEBUG
     * @apiVersion 0.1.0
     * @apiParam {String} dataViewCode 视图编码
     * @apiParam {Object} dataSourceParam 数据源参数
     * @apiParam {Object} dataViewParams 视图参数
     * @apiParam {Object} requestDataStructure 视图请求数据结构
     * @apiParam {Object} responseDataStructure 视图响应数据结构
     * @apiParam {Object} executionParams 调试参数
     * @apiParam (dataSourceParam) {String} dataSourceCode 数据源编码
     * @apiParam (dataSourceParam) {Object} dataSourceName 数据源名称
     * @apiParam (dataSourceParam) {String} dataSourceType 数据源类型（请求数据源类型接口）
     * @apiParam (dataSourceParam) {Object} dataSourceProperties 数据源配置
     * @apiParam (executionParams) {Object} params 执行参数
     * @apiParamExample {json} 请求参数示例
     * {
     * "dataViewCode": "test_view",
     * "dataViewParams": {
     * "command": ""
     * },
     * "dataSourceCode": "datasource_code",
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
     * }],
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
    Result<String> debug(@Validated @RequestBody DebugDataViewParams params);

}
