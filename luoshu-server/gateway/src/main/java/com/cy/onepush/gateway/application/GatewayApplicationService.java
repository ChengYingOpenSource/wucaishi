package com.cy.onepush.gateway.application;

import com.cy.onepush.common.framework.infrastructure.repository.page.PageInfo;
import com.cy.onepush.common.publishlanguage.gateway.GatewayId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.gateway.domain.Gateway;
import com.cy.onepush.gateway.domain.GatewayIdWithVersion;
import com.cy.onepush.gateway.domain.GatewayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GatewayApplicationService {

    private final GatewayRepository gatewayRepository;

    public Collection<Gateway> all() {
        return gatewayRepository.all();
    }

    public PageInfo<Gateway> pagination(String moduleCode, String name, Integer status, int pageNum, int pageSize) {
        return gatewayRepository.pagination(moduleCode, name, status, pageNum, pageSize);
    }

    public Gateway get(String gatewayCode, String version) {
        final GatewayIdWithVersion gatewayIdWithVersion = GatewayIdWithVersion.of(GatewayId.of(gatewayCode), Version.of(version));
        return gatewayRepository.get(gatewayIdWithVersion);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void publish(String gatewayCode, String version) {
        // get gateway and publish
        final GatewayIdWithVersion gatewayIdWithVersion = GatewayIdWithVersion.of(GatewayId.of(gatewayCode), Version.of(version));
        final Gateway gateway = gatewayRepository.get(gatewayIdWithVersion);
        gateway.publish();

        // update
        gatewayRepository.update(gateway);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void offline(String gatewayCode, String version) {
        // get gateway and offline
        final GatewayIdWithVersion gatewayIdWithVersion = GatewayIdWithVersion.of(GatewayId.of(gatewayCode), Version.of(version));
        final Gateway gateway = gatewayRepository.get(gatewayIdWithVersion);
        gateway.offline();

        // update
        gatewayRepository.update(gateway);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void delete(String gatewayCode, String version) {
        // get gateway and offline
        final GatewayIdWithVersion gatewayIdWithVersion = GatewayIdWithVersion.of(GatewayId.of(gatewayCode), Version.of(version));
        final Gateway gateway = gatewayRepository.get(gatewayIdWithVersion);

        // delete
        gatewayRepository.delete(gateway);
    }

}
