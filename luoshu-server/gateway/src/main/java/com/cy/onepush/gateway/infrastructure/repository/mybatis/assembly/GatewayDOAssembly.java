package com.cy.onepush.gateway.infrastructure.repository.mybatis.assembly;

import com.cy.onepush.common.publishlanguage.gateway.GatewayId;
import com.cy.onepush.common.publishlanguage.version.Version;
import com.cy.onepush.datapackager.domain.DataPackager;
import com.cy.onepush.gateway.domain.Gateway;
import com.cy.onepush.gateway.domain.GatewayStatus;
import com.cy.onepush.gateway.domain.Router;
import com.cy.onepush.gateway.infrastructure.repository.mybatis.bean.DataInterfaceDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface GatewayDOAssembly {

    GatewayDOAssembly ASSEMBLY = Mappers.getMapper(GatewayDOAssembly.class);

    default Router routerFromUriString(DataInterfaceDO dataInterfaceDO) {
        final Router router = new Router();
        router.defineUri(dataInterfaceDO.getMethod(), dataInterfaceDO.getUri());
        return router;
    }

    default Gateway domainFromDo(DataInterfaceDO dataInterfaceDO, DataPackager dataPackager) {
        final GatewayId gatewayId = GatewayId.of(dataInterfaceDO.getCode());
        final Gateway gateway = new Gateway(gatewayId, dataInterfaceDO.getName(), dataInterfaceDO.getMethod()
            , dataInterfaceDO.getUri(), Version.of(dataInterfaceDO.getVersion()));
        if (dataPackager != null) {
            gateway.bindDataPackager(dataPackager);
        }
        gateway.setGatewayStatus(GatewayStatus.of(dataInterfaceDO.getStatus()));
        gateway.setDescription(dataInterfaceDO.getDescription());
        gateway.setCreateTime(dataInterfaceDO.getGmtCreated());
        gateway.setModifyTime(dataInterfaceDO.getGmtModified());
        return gateway;
    }

    default List<Gateway> domainsFromDos(Collection<DataInterfaceDO> dataInterfaceDOS, Collection<DataPackager> dataPackagers) {
        final Map<String, DataPackager> dataPackagerMap = dataPackagers.stream().collect(Collectors.toMap(item -> item.getId().getId(), item -> item, (a, b) -> a));

        return dataInterfaceDOS.stream()
            .map(dataInterfaceDO -> GatewayDOAssembly.ASSEMBLY.domainFromDo(dataInterfaceDO, dataPackagerMap.get(dataInterfaceDO.getDataPackagerCode())))
            .collect(Collectors.toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", expression = "java( gateway.getId().getId() )")
    @Mapping(target = "name", source = "gateway.name")
    @Mapping(target = "uri", source = "gateway.uri")
    @Mapping(target = "method", source = "gateway.method")
    @Mapping(target = "dataPackagerCode", source = "gateway.dataPackager.id.id")
    @Mapping(target = "status", expression = "java( gateway.getGatewayStatus().getValue() )")
    @Mapping(target = "version", expression = "java( gateway.getGatewayVersion().getId() )")
    @Mapping(target = "description", source = "gateway.description")
    @Mapping(target = "gmtCreated", source = "date")
    @Mapping(target = "gmtModified", source = "date")
    @Mapping(target = "gmtCreator", expression = "java( -1L )")
    @Mapping(target = "gmtModifier", expression = "java( -1L )")
    DataInterfaceDO doFromDomain(Gateway gateway, Date date);

}
