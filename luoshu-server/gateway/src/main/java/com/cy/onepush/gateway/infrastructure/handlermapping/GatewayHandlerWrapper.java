package com.cy.onepush.gateway.infrastructure.handlermapping;

import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.common.publishlanguage.gateway.GatewayId;
import com.cy.onepush.gateway.domain.Gateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Slf4j
public class GatewayHandlerWrapper {

    private final Gateway gateway;

    public GatewayHandlerWrapper(Gateway gateway) {
        this.gateway = gateway;
    }

    public GatewayId getId() {
        return this.gateway.getId();
    }

    public String getUri() {
        return this.gateway.getUri();
    }

    public String getMethod() {
        return this.gateway.getMethod();
    }

    public ResponseEntity<Result<Object>> execute(@RequestBody Map<String, Object> params) {
        final Object result = this.gateway.execute(params);
        return ResponseEntity.ok(Result.builder().success(result).build());
    }

}
