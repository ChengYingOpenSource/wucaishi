package com.cy.onepush.project.interfaces;

import com.cy.onepush.common.framework.infrastructure.web.PaginationResult;
import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.project.interfaces.params.CreateDataSourceParams;
import com.cy.onepush.project.interfaces.params.SearchDataSourceParams;
import com.cy.onepush.project.interfaces.params.UpdateDataSourceParams;
import com.cy.onepush.datasource.interfaces.vo.DataSourceVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/api/v1/projects/{projectCode}/datasource")
public interface ProjectDataSourceApi {

    /**
     * @api {GET} /api/v1/projects/{projectCode}/datasource 获取项目数据源
     * @apiGroup ProjectDataSourceApi
     * @apiName 获取项目数据源
     * @apiDescription 获取某个项目下的数据源
     * @apiVersion 0.1.0
     * @apiParam {String} [type] 数据源类型（根据数据源类型接口获取）
     * @apiParam {String} [name] 数据源名称（模糊搜索）
     * @apiParam {String} [code] 数据源编码（模糊搜索）
     * @apiParam {Number} pageSize 获取页大小
     * @apiParam {Number} pageNum 当前页
     * @apiParamExample {json} 请求参数示例
     * {
     * "type": "JDBC",
     * "name": "test_da",
     * "code": "test_code",
     * "pageSize": 10,
     * "pageNum": 1
     * }
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Object} data 数据
     * @apiSuccess (data) {Array} list 数据
     * @apiSuccess (data) {Object} pagination 数据
     * @apiSuccess (list) {String} code 数据源编码
     * @apiSuccess (list) {String} name 数据源名称
     * @apiSuccess (list) {String} type 数据源类型
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
     * "name": "datasource",
     * "code": "jdbc_code",
     * "type": "JDBC",
     * "createTime": "2020-11-30 10:30:55",
     * "updateTime": "2020-11-30 10:30:55"
     * },
     * {
     * "name": "http",
     * "code": "http_code",
     * "type": "HTTP",
     * "createTime": "2020-11-30 10:30:55",
     * "updateTime": "2020-11-30 10:30:55"
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
    PaginationResult<DataSourceVO> list(@PathVariable("projectCode") String projectCode,
                                        @Validated SearchDataSourceParams params);

    /**
     * @api {POST} /api/v1/projects/{projectCode}/datasource 创建数据源
     * @apiGroup ProjectDataSourceApi
     * @apiName 创建数据源
     * @apiDescription 创建某个项目的数据源
     * @apiVersion 0.1.0
     * @apiParam {Object} dataSourceCode 数据源编码
     * @apiParam {Object} dataSourceName 数据源名称
     * @apiParam {String} dataSourceType 数据源类型（请求数据源类型接口）
     * @apiParam {Object} dataSourceProperties 数据源配置
     * @apiParamExample {json} 请求参数示例
     * {
     * "dataSourceCode": "test_datasource",
     * "dataSourceName": "test_datasource",
     * "dataSourceType": "JDBC",
     * "dataSourceProperties": {
     * "url": "jdbc:mysql://xxx:3306/xxx_aaa?xxx=xxx",
     * "username": "xxx",
     * "password": "xxx",
     * "driverClass": "com.xxxx",
     * }
     * }
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {String} data 数据源编码
     * @apiSuccessExample {json} Success-Response:
     * {"code":0,"success":true,"msg":"","data": "test_datasource" }
     */
    @PostMapping
    Result<String> create(@PathVariable("projectCode") String projectCode,
                          @Validated @RequestBody CreateDataSourceParams params);

    /**
     * @api {PUT} /api/v1/projects/{projectCode}/datasource/{dataSourceCode} 修改数据源
     * @apiGroup ProjectDataSourceApi
     * @apiName 修改数据源
     * @apiDescription 修改某个项目的数据源
     * @apiVersion 0.1.0
     * @apiParam {Object} dataSourceName 数据源名称
     * @apiParam {String} dataSourceType 数据源类型（请求数据源类型接口）
     * @apiParam {Object} dataSourceProperties 数据源配置
     * @apiParamExample {json} 请求参数示例
     * {
     * "dataSourceName": "test_datasource",
     * "dataSourceType": "JDBC",
     * "dataSourceProperties": {
     * "url": "jdbc:mysql://xxx:3306/xxx_aaa?xxx=xxx",
     * "username": "xxx",
     * "password": "xxx",
     * "driverClass": "com.xxxx",
     * }
     * }
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Boolean} data 修改成功
     * @apiSuccessExample {json} Success-Response:
     * {"code":0,"success":true,"msg":"","data": true }
     */
    @PutMapping(path = "/{dataSourceCode}")
    Result<Boolean> update(@PathVariable("projectCode") String projectCode,
                           @PathVariable("dataSourceCode") String dataSourceCode,
                           @Validated @RequestBody UpdateDataSourceParams params);

}
