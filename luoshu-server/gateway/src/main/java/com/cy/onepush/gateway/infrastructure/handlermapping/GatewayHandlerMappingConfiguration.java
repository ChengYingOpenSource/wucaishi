package com.cy.onepush.gateway.infrastructure.handlermapping;

import com.cy.onepush.gateway.domain.GatewayRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * gateway handler mapping configuration
 *
 * @author WhatAKitty
 * @date 2020-12-20
 * @since 0.1.0
 */
@Configuration
public class GatewayHandlerMappingConfiguration {

    @Bean
    public GatewayHandlerMappingAdapter gatewayHandlerMappingAdapter(RequestMappingHandlerMapping requestMappingHandlerMapping,
                                                                     GatewayRepository gatewayRepository) {
        return new GatewayHandlerMappingAdapter(requestMappingHandlerMapping, gatewayRepository);
    }


}
