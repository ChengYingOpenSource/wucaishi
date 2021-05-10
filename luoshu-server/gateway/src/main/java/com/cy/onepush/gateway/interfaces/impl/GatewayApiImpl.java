package com.cy.onepush.gateway.interfaces.impl;

import com.cy.onepush.common.framework.infrastructure.repository.page.PageInfo;
import com.cy.onepush.common.framework.infrastructure.web.PaginationResult;
import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.gateway.application.GatewayApplicationService;
import com.cy.onepush.gateway.domain.Gateway;
import com.cy.onepush.gateway.interfaces.GatewayApi;
import com.cy.onepush.gateway.interfaces.assembly.GatewayVOAssembly;
import com.cy.onepush.gateway.interfaces.params.SearchGatewayParams;
import com.cy.onepush.gateway.interfaces.vo.GatewayVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GatewayApiImpl implements GatewayApi {

    private final GatewayApplicationService gatewayApplicationService;

    @Override
    public PaginationResult<GatewayVO> list(SearchGatewayParams params) {
        final PageInfo<Gateway> pagination = gatewayApplicationService.pagination(params.getApp(), params.getName(), params.getStatus(),
            params.getPageNum(), params.getPageSize());
        final List<GatewayVO> gatewayVOS = GatewayVOAssembly.ASSEMBLY.vosFromDomains(pagination.getList(), new Date());

        return PaginationResult.<GatewayVO>paginationBuilder()
            .list(gatewayVOS)
            .pagination(params.getPageNum(), params.getPageSize(), pagination.getTotal())
            .build();
    }

    @Override
    public Result<Boolean> delete(String gatewayCode, String version) {
        gatewayApplicationService.delete(gatewayCode, version);
        return Result.<Boolean>builder().success(Boolean.TRUE).build();
    }

    @Override
    public Result<Boolean> publish(String gatewayCode, String version) {
        gatewayApplicationService.publish(gatewayCode, version);
        return Result.<Boolean>builder().success(Boolean.TRUE).build();
    }

    @Override
    public Result<Boolean> offline(String gatewayCode, String version) {
        gatewayApplicationService.offline(gatewayCode, version);
        return Result.<Boolean>builder().success(Boolean.TRUE).build();
    }

    @Override
    public Result<Boolean> existed(String gatewayCode, String version) {
        final Gateway gateway = gatewayApplicationService.get(gatewayCode, version);
        return Result.<Boolean>builder().success(gateway != null).build();
    }

}
