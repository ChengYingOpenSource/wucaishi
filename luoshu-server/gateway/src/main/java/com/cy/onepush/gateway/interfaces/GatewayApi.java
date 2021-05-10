package com.cy.onepush.gateway.interfaces;

import com.cy.onepush.common.framework.infrastructure.web.PaginationResult;
import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.gateway.interfaces.params.SearchGatewayParams;
import com.cy.onepush.gateway.interfaces.vo.GatewayVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * gateway api
 *
 * @author WhatAKitty
 * @date 2020-12-16
 * @since 0.1.0
 */
@RequestMapping(path = "/api/v1/gateways")
public interface GatewayApi {

    /**
     * @api {GET} /api/v1/gateways 获取接口列表
     * @apiGroup GatewayApi
     * @apiName 获取接口列表
     * @apiDescription 获取接口列表
     * @apiVersion 0.1.0
     * @apiParam {Number} pageSize 获取页大小
     * @apiParam {Number} pageNum 当前页
     * @apiParam {Number=0,1} [status] 接口状态（0：待上线；1：已上线）
     * @apiParam {String} [name] 名称（模糊搜索）
     * @apiParam {String} [version] 版本
     * @apiParamExample {json} 请求参数示例
     * {
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
    PaginationResult<GatewayVO> list(@Validated SearchGatewayParams params);

    /**
     * @api {DELETE} /api/v1/gateways/{gatewayCode}/{version} 删除接口
     * @apiGroup GatewayApi
     * @apiName 删除接口
     * @apiDescription 删除编码为{gatewayCode}的接口
     * @apiVersion 0.1.0
     * @apiParam {String} apiKey 接口编码
     * @apiParamExample {json} 请求参数示例
     * {
     * "apiKey": "test_module_api"
     * }
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Boolean} data 是否删除成功
     * @apiSuccessExample {json} Success-Response:
     * {"code":0,"success":true,"msg":"","data": true }
     * @apiError (- 404) {Number} code 资源不存在
     * @apiErrorExample {json} -404
     * {
     * "code": -404,
     * "msg": "the resources not exists",
     * "success": false
     * }
     */
    @DeleteMapping(path = "/{gatewayCode}/{version}")
    Result<Boolean> delete(@PathVariable("gatewayCode") String gatewayCode, @PathVariable("version") String version);

    /**
     * @api {PUT} /api/v1/gateways/{gatewayCode}/{version}/published 接口发布
     * @apiGroup GatewayApi
     * @apiName 接口发布
     * @apiDescription 发布编码为{gatewayCode}的接口
     * @apiVersion 0.1.0
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Boolean} data 是否发布成功
     * @apiSuccessExample {json} Success-Response:
     * {"code":0,"success":true,"msg":"","data": true }
     * @apiError (- 404) {Number} code 资源不存在
     * @apiErrorExample {json} -404
     * {
     * "code": -404,
     * "msg": "the resources not exists",
     * "success": false
     * }
     */
    @PutMapping(path = "/{gatewayCode}/{version}/published")
    Result<Boolean> publish(@PathVariable("gatewayCode") String gatewayCode, @PathVariable("version") String version);

    /**
     * @api {PUT} /api/v1/gateways/{gatewayCode}/{version}/offline 接口下线
     * @apiGroup GatewayApi
     * @apiName 接口下线
     * @apiDescription 下线编码为{gatewayCode}的接口
     * @apiVersion 0.1.0
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Boolean} data 是否下线成功
     * @apiSuccessExample {json} Success-Response:
     * {"code":0,"success":true,"msg":"","data": true }
     * @apiError (- 404) {Number} code 资源不存在
     * @apiErrorExample {json} -404
     * {
     * "code": -404,
     * "msg": "the resources not exists",
     * "success": false
     * }
     */
    @PutMapping(path = "/{gatewayCode}/{version}/offline")
    Result<Boolean> offline(@PathVariable("gatewayCode") String gatewayCode, @PathVariable("version") String version);

    /**
     * @api {GET} /api/v1/gateways/{gatewayCode}/{version}/existed 接口编码判重
     * @apiGroup GatewayApi
     * @apiName 接口编码判重
     * @apiDescription 编码为{gatewayCode}的接口是否存在
     * @apiVersion 0.1.0
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Boolean} data 是否存在
     * @apiSuccessExample {json} Success-Response:
     * {"code":0,"success":true,"msg":"","data": false }
     */
    @GetMapping(path = "/{gatewayCode}/{version}/existed")
    Result<Boolean> existed(@PathVariable("gatewayCode") String gatewayCode, @PathVariable("version") String version);

}
