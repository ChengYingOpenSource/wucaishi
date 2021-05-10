package com.cy.onepush.project.interfaces;

import com.cy.onepush.common.framework.infrastructure.web.PaginationResult;
import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.project.interfaces.params.CreateModuleParams;
import com.cy.onepush.project.interfaces.params.SearchModuleParams;
import com.cy.onepush.project.interfaces.params.UpdateModuleParams;
import com.cy.onepush.project.interfaces.vo.ProjectModuleVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/api/v1/projects/{projectCode}/modules")
public interface ProjectModulesApi {

    /**
     * @api {GET} /api/v1/projects/{projectCode}/modules 获取子模块列表
     * @apiGroup ModuleApi
     * @apiName 获取子模块列表
     * @apiDescription 获取某个项目下的子模块列表
     * @apiVersion 0.1.0
     * @apiParam {String} [name] 模块名（模糊查询）
     * @apiParam {Number} pageSize 获取页大小
     * @apiParam {Number} pageNum 当前页
     * @apiParamExample {json} 请求参数示例
     * {
     * "name": "测试模块",
     * "pageSize": 10,
     * "pageNum": 1
     * }
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Object} data 数据
     * @apiSuccess (data) {Array} list 数据
     * @apiSuccess (data) {Object} pagination 数据
     * @apiSuccess (list) {String} projectCode 项目编码
     * @apiSuccess (list) {String} moduleCode 模块编码
     * @apiSuccess (list) {String} name 模块名称
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
     * "id": 12,
     * "name": "例子",
     * "moduleCode": "examplez",
     * "createTime": "2020-12-01 11:21:58",
     * "updateTime": "2020-12-01 11:21:58",
     * "projectCode": "test"
     * },
     * {
     * "id": 2,
     * "name": "示例1",
     * "moduleCode": "example",
     * "createTime": "2020-12-01 14:06:18",
     * "updateTime": "2020-12-01 14:06:18",
     * "projectCode": "test"
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
    PaginationResult<ProjectModuleVO> list(@PathVariable("projectCode") String projectCode, @Validated SearchModuleParams params);

    /**
     * @api {POST} /api/v1/projects/{projectCode}/modules 创建子模块
     * @apiGroup ModuleApi
     * @apiName 创建子模块
     * @apiDescription 创建某项目下的子模块
     * @apiVersion 0.1.0
     * @apiParam {String} code 子模块编码
     * @apiParam {String} name 子模块名
     * @apiParam {String} description 子模块描述
     * @apiParamExample {json} 请求参数示例
     * {
     * "code": "test_module_code"
     * "name": "测试模块",
     * "description": "模块描述"
     * }
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {String} data 项目编码
     * @apiSuccessExample {json} Success-Response:
     * {"code":0,"success":true,"msg":"","data": "intra" }
     */
    @PostMapping
    Result<String> add(@PathVariable("projectCode") String projectCode, @Validated @RequestBody CreateModuleParams params);

    /**
     * @api {PUT} /api/v1/projects/{projectCode}/modules/{moduleCode} 修改子模块
     * @apiGroup ModuleApi
     * @apiName 修改子模块
     * @apiDescription 修改某项目下模块编码为{moduleCode}的子模块
     * @apiVersion 0.1.0
     * @apiParam {String} name 模块名
     * @apiParam {String} description 模块描述
     * @apiParamExample {json} 请求参数示例
     * {
     * "name": "测试模块",
     * "description": "模块描述"
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
    @PutMapping(path = "/{moduleCode}")
    Result<Boolean> update(@PathVariable("projectCode") String projectCode,
                           @PathVariable("moduleCode") String moduleCode,
                           @Validated @RequestBody UpdateModuleParams params);

    /**
     * @api {DELETE} /api/v1/projects/{projectCode}/modules/{moduleCode} 删除子模块
     * @apiGroup ModuleApi
     * @apiName 删除子模块
     * @apiDescription 删除某个项目下编码为{moduleCode}的子模块
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
    @DeleteMapping(path = "/{moduleCode}")
    Result<Boolean> delete(@PathVariable("projectCode") String projectCode, @PathVariable("moduleCode") String moduleCode);

}
