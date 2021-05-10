package com.cy.onepush.gateway.interfaces.assembly;

import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.gateway.domain.Gateway;
import com.cy.onepush.gateway.domain.Router;
import com.cy.onepush.gateway.interfaces.vo.GatewayVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface GatewayVOAssembly {

    GatewayVOAssembly ASSEMBLY = Mappers.getMapper(GatewayVOAssembly.class);

    @Mapping(target = "project", ignore = true)
    @Mapping(target = "app", ignore = true)
    @Mapping(target = "apiKey", source = "gateway.id.id")
    @Mapping(target = "version", source = "gateway.gatewayVersion.id")
    @Mapping(target = "description", source = "gateway.description")
    @Mapping(target = "url", expression = "java( String.format(\"%s %s\", gateway.getMethod(), gateway.getUri()) )")
    @Mapping(target = "createTime", source = "date")
    @Mapping(target = "updateTime", source = "date")
    @Mapping(target = "dataViewers", source = "gateway.dataPackager.dataViews")
    GatewayVO voFromDomain(Gateway gateway, Date date);

    default List<GatewayVO> vosFromDomains(Collection<Gateway> collection, Date date) {
        return collection.parallelStream().map(item -> GatewayVOAssembly.ASSEMBLY.voFromDomain(item, date)).collect(Collectors.toList());
    }

    @Mapping(target = "dataViewCode", source = "id.id")
    @Mapping(target = "dataSourceName", source = "name")
    @Mapping(target = "dataViewType", source = "dataSource.type")
    GatewayVO.GatewayDataViewerDTO dataViewDtoFromDomain(DataView dataView);

    default List<GatewayVO.GatewayDataViewerDTO> dataViewDtosFromDomains(Map<String, DataView> map) {
        return map.values().stream()
            .map(GatewayVOAssembly.ASSEMBLY::dataViewDtoFromDomain)
            .collect(Collectors.toList());
    }

}
