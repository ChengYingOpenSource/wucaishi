package com.cy.onepush.console.interfaces;

import com.cy.onepush.common.framework.infrastructure.web.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(path = "/api/v1/consoles")
public interface ConsoleApi {

    /**
     * @api {GET} /api/v1/consoles/{consoleCode}/logs 获取控制台日志
     * @apiGroup ConsoleApi
     * @apiName 获取控制台日志
     * @apiDescription 获取控制台日志
     * @apiVersion 0.1.0
     * @apiSuccess {Number} code 响应码
     * @apiSuccess {Boolean} success 接口是否成功
     * @apiSuccess {String} msg 响应消息
     * @apiSuccess {Array} data 日志列表
     * @apiSuccessExample {json} Success-Response:
     * {
     * "code": 0,
     * "success": true,
     * "msg": "",
     * "data": [
     * "[INFO] xxxxxxx",
     * "[WARN] xxxxx"
     * ]
     */
    @GetMapping(path = "/{consoleCode}/logs")
    Result<List<String>> logs(@PathVariable("consoleCode") String consoleCode);

}
