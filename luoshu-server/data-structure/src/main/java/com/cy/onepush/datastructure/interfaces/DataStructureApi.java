package com.cy.onepush.datastructure.interfaces;

import com.cy.onepush.common.framework.infrastructure.web.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(path = "/api/v1/datastructures")
public interface DataStructureApi {

    /**
     * @api {GET} /api/v1/datastructures/types 数据格式类型
     * @apiGroup DataStructureApi
     * @apiName 数据格式类型
     * @apiDescription 数据格式类型
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
     * "data": ["INT", "DECIMAL", "STRING", "DATE", "COLLECTION", "ITEM"]
     * }
     * @return
     */
    @GetMapping(path = "/types")
    Result<List<String>> types();
    
}
