package com.cy.onepush.gateway.infrastructure.handlermapping;

import com.cy.onepush.common.publishlanguage.gateway.GatewayId;
import com.cy.onepush.gateway.domain.Gateway;
import com.cy.onepush.gateway.domain.GatewayRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@RequiredArgsConstructor
public class GatewayHandlerMappingAdapter {

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;
    private final GatewayRepository gatewayRepository;

    private final Map<GatewayId, RequestMappingInfo> REGISTER_CACHED_HOLDER = new HashMap<>(64);
    private final ReentrantLock LOCK = new ReentrantLock(false);

    public void register(GatewayHandlerWrapper gateway) {
        LOCK.lock();

        try {
            final RequestMappingInfo requestMappingInfo = RequestMappingInfo
                .paths(gateway.getUri())
                .methods(RequestMethod.valueOf(gateway.getMethod()))
                .mappingName(getMappingName(gateway))
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .build();

            try {
                if (REGISTER_CACHED_HOLDER.containsKey(gateway.getId())) {
                    // already registered
                    log.error("the uri {} {} has been registered, please check another one", gateway.getMethod(), gateway.getUri());
                    return;
                }

                // register and cache
                requestMappingHandlerMapping.registerMapping(requestMappingInfo, gateway, GatewayHandlerWrapper.class.getDeclaredMethod("execute", Map.class));
                REGISTER_CACHED_HOLDER.put(gateway.getId(), requestMappingInfo);
            } catch (NoSuchMethodException e) {
                // ignore
            }
        } finally {
            LOCK.unlock();
        }
    }

    public void unregister(GatewayHandlerWrapper gateway) {
        LOCK.lock();

        try {
            final RequestMappingInfo requestMappingInfo = REGISTER_CACHED_HOLDER.get(gateway.getId());

            if (requestMappingInfo != null) {
                // unregister and remove from cache
                requestMappingHandlerMapping.unregisterMapping(requestMappingInfo);
                REGISTER_CACHED_HOLDER.remove(gateway.getId());
            }
        } finally {
            LOCK.unlock();
        }
    }

    public void refresh(GatewayHandlerWrapper gateway) {
        LOCK.lock();

        try {
            final RequestMappingInfo requestMappingInfo = REGISTER_CACHED_HOLDER.get(gateway.getId());

            if (requestMappingInfo == null) {
                log.warn("cannot refresh due to offline maybe. {} {}", gateway.getMethod(), gateway.getUri());
                return;
            }

            // existed and refreshed
            this.unregister(gateway);
            this.register(gateway);
        } finally {
            LOCK.unlock();
        }
    }

    private String getMappingName(GatewayHandlerWrapper gateway) {
        return String.format("Gateway_%s", gateway.getId().getId());
    }

    public void init() {
        final Collection<Gateway> gateways = gatewayRepository.allPublished();

        // register gateways
        gateways.forEach(gateway -> this.register(new GatewayHandlerWrapper(gateway)));
    }

}
