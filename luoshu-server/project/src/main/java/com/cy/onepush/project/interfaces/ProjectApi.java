package com.cy.onepush.project.interfaces;

import com.cy.onepush.common.framework.infrastructure.web.PaginationResult;
import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.project.interfaces.params.CreateProjectParams;
import com.cy.onepush.project.interfaces.params.SearchProjectParams;
import com.cy.onepush.project.interfaces.params.UpdateProjectParams;
import com.cy.onepush.project.interfaces.vo.ProjectVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * gateway api
 *
 * @author WhatAKitty
 * @date 2020-12-21
 * @since 0.1.0
 */
@RequestMapping(path = "/api/v1/projects")
public interface ProjectApi {

    /**
     * @api {GET} /api/v1/projects 获取项目列表
     * @apiGroup ProjectApi
     * @apiName 获取项目列表
     * @apiDescription 获取项目列表
     * @apiVersion 0.1.0
     * @apiParam {String} [name] 项目名（模糊查询）
     * @apiParam {Number} pageSize 获取页大小
     * @apiParam {Number} pageNum 当前页
     * @apiParamExample {json} 请求参数示例
     * {
     * "name": "测试项目",
     * "pageSize": 10,
     * "pageNum": 1
     * }
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Object} data 数据
     * @apiSuccess (data) {Array} list 数据
     * @apiSuccess (data) {Object} pagination 数据
     * @apiSuccess (list) {String} code 项目编码
     * @apiSuccess (list) {String} name 项目名称
     * @apiSuccess (list) {String} description 项目描述
     * @apiSuccess (list) {String} createTime 创建时间
     * @apiSuccess (list) {String} updateTime 修改时间
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
     * "name": "project1",
     * "code": "project1",
     * "description": "",
     * "createTime": "2020-11-30 10:30:55",
     * "updateTime": "2020-11-30 10:30:55"
     * },
     * {
     * "name": "project2",
     * "code": "project2",
     * "description": "",
     * "createTime": "2020-11-30 10:30:52",
     * "updateTime": "2020-11-30 10:30:52"
     * },
     * {
     * "name": "project3",
     * "code": "project3",
     * "description": "",
     * "createTime": "2020-11-30 10:29:29",
     * "updateTime": "2020-11-30 10:29:29"
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
    PaginationResult<ProjectVO> list(@Validated SearchProjectParams params);

    /**
     * @api {POST} /api/v1/projects 创建项目
     * @apiGroup ProjectApi
     * @apiName 创建项目
     * @apiDescription 创建项目
     * @apiVersion 0.1.0
     * @apiParam {String} code 项目编码
     * @apiParam {String} name 项目名
     * @apiParam {String} description 获取页大小
     * @apiParamExample {json} 请求参数示例
     * {
     * "code": "test_project"
     * "name": "测试项目",
     * "description": "项目描述"
     * }
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {String} data 项目编码
     * @apiSuccessExample {json} Success-Response:
     * {"code":0,"success":true,"msg":"","data": "intra" }
     */
    @PostMapping
    Result<String> create(@Validated @RequestBody CreateProjectParams createProjectParams);

    /**
     * @api {PUT} /api/v1/projects/{projectCode} 修改项目
     * @apiGroup ProjectApi
     * @apiName 修改项目
     * @apiDescription 修改项目名称或是描述
     * @apiVersion 0.1.0
     * @apiParam {String} name 项目名
     * @apiParam {String} description 项目描述
     * @apiParamExample {json} 请求参数示例
     * {
     * "name": "测试项目",
     * "description": "项目描述"
     * }
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Boolean} data 是否修改成功
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
    @PutMapping(path = "/{projectCode}")
    Result<Boolean> update(@PathVariable("projectCode") String projectCode, @Validated @RequestBody UpdateProjectParams params);

    /**
     * @api {DELETE} /api/v1/projects/{projectCode} 删除项目
     * @apiGroup ProjectApi
     * @apiName 删除项目
     * @apiDescription 根据项目编码删除项目
     * @apiVersion 0.1.0
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
    @DeleteMapping(path = "/{projectCode}")
    Result<Boolean> delete(@PathVariable("projectCode") String projectCode);

}
