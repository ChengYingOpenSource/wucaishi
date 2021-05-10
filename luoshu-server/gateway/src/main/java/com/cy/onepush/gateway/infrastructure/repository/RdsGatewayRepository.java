package com.cy.onepush.gateway.infrastructure.repository;

import com.cy.onepush.common.exception.DuplicateResourceException;
import com.cy.onepush.common.exception.ResourceNotFoundException;
import com.cy.onepush.common.framework.infrastructure.repository.page.PageHelper;
import com.cy.onepush.common.framework.infrastructure.repository.page.PageInfo;
import com.cy.onepush.common.publishlanguage.datapackager.DataPackagerId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.common.utils.DomainEventUtils;
import com.cy.onepush.datapackager.domain.DataPackager;
import com.cy.onepush.datapackager.domain.DataPackagerIdWithVersion;
import com.cy.onepush.datapackager.domain.DataPackagerRepository;
import com.cy.onepush.gateway.domain.Gateway;
import com.cy.onepush.gateway.domain.GatewayIdWithVersion;
import com.cy.onepush.gateway.domain.GatewayRepository;
import com.cy.onepush.gateway.domain.GatewayStatus;
import com.cy.onepush.gateway.domain.event.GatewayUpdatedEvent;
import com.cy.onepush.gateway.infrastructure.repository.mybatis.assembly.GatewayDOAssembly;
import com.cy.onepush.gateway.infrastructure.repository.mybatis.bean.DataInterfaceDO;
import com.cy.onepush.gateway.infrastructure.repository.mybatis.mapper.DataInterfaceMapper;
import com.cy.onepush.gateway.infrastructure.repository.mybatis.params.SearchDataInterfacesDOParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RdsGatewayRepository implements GatewayRepository {

    private final DataInterfaceMapper dataInterfaceMapper;

    private final DataPackagerRepository dataPackagerRepository;

    @Override
    public Collection<Gateway> all() {
        final List<DataInterfaceDO> dataInterfaceDOS = dataInterfaceMapper.selectAll();

        final List<DataPackager> dataPackagers = dataInterfaceDOS.stream()
            .map(dataInterfaceDO -> {
                final DataPackagerIdWithVersion dataPackagerIdWithVersion = DataPackagerIdWithVersion.of(
                    DataPackagerId.of(dataInterfaceDO.getDataPackagerCode()),
                    Version.of(dataInterfaceDO.getVersion())
                );
                return dataPackagerRepository.getDataPackager(dataPackagerIdWithVersion);
            })
            .collect(Collectors.toList());

        return GatewayDOAssembly.ASSEMBLY.domainsFromDos(dataInterfaceDOS, dataPackagers);
    }

    @Override
    public Collection<Gateway> allPublished() {
        return all().parallelStream()
            .filter(item -> GatewayStatus.PUBLISHED.equals(item.getGatewayStatus()))
            .collect(Collectors.toList());
    }

    @Override
    public PageInfo<Gateway> pagination(String moduleCode, String name, Integer status, int pageNum, int pageSize) {
        final SearchDataInterfacesDOParams params = new SearchDataInterfacesDOParams();
        params.setModuleCode(moduleCode);
        params.setName(name);
        params.setStatus(status);
        final PageInfo<DataInterfaceDO> page = PageHelper.doSelectPage(pageNum, pageSize, () -> dataInterfaceMapper.selectByParams(params));

        final List<DataPackager> dataPackagers = page.getList().stream()
            .map(dataInterfaceDO -> {
                final DataPackagerIdWithVersion dataPackagerIdWithVersion = DataPackagerIdWithVersion.of(
                    DataPackagerId.of(dataInterfaceDO.getDataPackagerCode()),
                    Version.of(dataInterfaceDO.getVersion())
                );
                return dataPackagerRepository.getDataPackager(dataPackagerIdWithVersion);
            })
            .collect(Collectors.toList());
        final List<Gateway> gateways = GatewayDOAssembly.ASSEMBLY.domainsFromDos(page.getList(), dataPackagers);

        final PageInfo<Gateway> result = new PageInfo<>(pageNum, pageSize);
        result.setTotal(page.getTotal());
        result.setList(gateways);
        return result;
    }

    @Override
    public Gateway get(GatewayIdWithVersion gatewayIdWithVersion) {
        final String gatewayCode = gatewayIdWithVersion.getGatewayId().getId();
        final String gatewayVersion = gatewayIdWithVersion.getVersion().getId();
        final DataInterfaceDO dataInterfaceDO = dataInterfaceMapper.selectByCodeAndVersion(gatewayCode, gatewayVersion);
        if (dataInterfaceDO == null) {
            return null;
        }

        final DataPackagerIdWithVersion dataPackagerIdWithVersion = DataPackagerIdWithVersion.of(
            DataPackagerId.of(dataInterfaceDO.getDataPackagerCode()),
            Version.of(dataInterfaceDO.getVersion())
        );
        final DataPackager dataPackager = dataPackagerRepository.getDataPackager(dataPackagerIdWithVersion);

        return GatewayDOAssembly.ASSEMBLY.domainFromDo(dataInterfaceDO, dataPackager);
    }

    @Override
    public void add(Gateway gateway) {
        final Date date = new Date();

        synchronized (gateway.getIdWithVersion().toString().intern()) {
            final DataInterfaceDO old = dataInterfaceMapper.selectByCodeAndVersion(gateway.getId().getId(), gateway.getGatewayVersion().getId());
            if (old != null) {
                throw new DuplicateResourceException("the data interface %s has been registered", gateway.getIdWithVersion().toString());
            }

            // persist data interface
            final DataInterfaceDO dataInterfaceDO = GatewayDOAssembly.ASSEMBLY.doFromDomain(gateway, date);
            dataInterfaceMapper.insert(dataInterfaceDO);
        }

        // persist data packager
        dataPackagerRepository.add(gateway.getDataPackager());
    }

    @Override
    public void update(Gateway gateway) {
        final Date date = new Date();

        synchronized (gateway.getIdWithVersion().toString().intern()) {
            final DataInterfaceDO old = dataInterfaceMapper.selectByCodeAndVersion(gateway.getId().getId(), gateway.getGatewayVersion().getId());
            if (old == null) {
                throw new ResourceNotFoundException("the data interface %s not found", gateway.getIdWithVersion().toString());
            }

            // update data interface
            final DataInterfaceDO dataInterfaceDO = GatewayDOAssembly.ASSEMBLY.doFromDomain(gateway, date);
            dataInterfaceMapper.insertOrUpdate(dataInterfaceDO);
        }

        // update data packager
        dataPackagerRepository.update(gateway.getDataPackager());

        // publish refresh event
        DomainEventUtils.publishEvent(new GatewayUpdatedEvent(gateway));
    }

    @Override
    public void delete(Gateway gateway) {
        synchronized (gateway.getIdWithVersion().toString().intern()) {
            final DataInterfaceDO old = dataInterfaceMapper.selectByCodeAndVersion(gateway.getId().getId(), gateway.getGatewayVersion().getId());
            if (old == null) {
                throw new ResourceNotFoundException("the data interface %s not found", gateway.getIdWithVersion().toString());
            }

        }

        // TODO 删除dataPackager等附属数据
        dataPackagerRepository.delete(gateway.getDataPackager());

        dataInterfaceMapper.deleteByCodeAndVersion(gateway.getId().getId(), gateway.getGatewayVersion().getId());
    }

}
