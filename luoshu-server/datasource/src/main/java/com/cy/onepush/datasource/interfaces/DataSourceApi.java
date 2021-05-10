package com.cy.onepush.datasource.interfaces;

import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.datasource.interfaces.params.ValidateDataSourceParams;
import com.cy.onepush.datasource.interfaces.vo.DataSourceValidateVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "/api/v1/datasource")
public interface DataSourceApi {

    /**
     * @api {GET} /api/v1/datasource/types 数据源类型
     * @apiGroup DataSourceApi
     * @apiName 数据源类型
     * @apiDescription 数据源类型
     * @apiVersion 0.1.0
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Array} data 数据源类型列表
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
     * @api {POST} /api/v1/datasource/{datasourceCode}/validated 数据源验证
     * @apiGroup DataSourceApi
     * @apiName 数据源验证
     * @apiDescription 验证数据源是否可用
     * @apiVersion 0.1.0
     * @apiParam {String} dataSourceType 数据源类型（请求数据源类型接口）
     * @apiParam {Object} dataSourceProperties 数据源配置
     * @apiParamExample {json} 请求参数
     * {
     * "dataSourceType": "",
     * "dataSourceProperties: {
     * "host": "127.0.0.1",
     * "port": "80"
     * }
     * }
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Object} data 验证结果
     * @apiSuccess (data) {Boolean} ok 验证结果
     * @apiSuccess (data) {String} msg 验证结果
     * @apiSuccessExample {json} Success-Response:
     * {
     * "code": 0,
     * "success": true,
     * "msg": "",
     * "data": {
     * "ok": true,,
     * "msg": "bad auth"
     * }
     * }
     */
    @PostMapping(path = "/{datasourceCode}/validated")
    Result<DataSourceValidateVO> validate(@PathVariable("datasourceCode") String datasourceCode, @Validated @RequestBody ValidateDataSourceParams params);

}
