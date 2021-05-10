package com.cy.onepush.project.interfaces.assembly;

import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.gateway.domain.Gateway;
import com.cy.onepush.project.interfaces.vo.ModuleGatewayVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper
public interface ModuleGatewayVOAssembly {

    ModuleGatewayVOAssembly ASSEMBLY = Mappers.getMapper(ModuleGatewayVOAssembly.class);

    @Mapping(target = "project", source = "projectCode")
    @Mapping(target = "app", source = "moduleCode")
    @Mapping(target = "apiKey", source = "gateway.id.id")
    @Mapping(target = "version", source = "gateway.gatewayVersion.id")
    @Mapping(target = "description", source = "gateway.description")
    @Mapping(target = "url", expression = "java( String.format(\"%s %s\", gateway.getMethod(), gateway.getUri()) )")
    @Mapping(target = "createTime", source = "gateway.createTime")
    @Mapping(target = "updateTime", source = "gateway.modifyTime")
    @Mapping(target = "dataViewers", source = "gateway.dataPackager.dataViews")
    @Mapping(target = "status", source = "gateway.gatewayStatus.value")
    ModuleGatewayVO voFromDomain(String projectCode, String moduleCode, Gateway gateway);

    default List<ModuleGatewayVO> vosFromDomains(String projectCode, String moduleCode, Collection<Gateway> collection) {
        return collection.parallelStream()
            .map(item -> ModuleGatewayVOAssembly.ASSEMBLY.voFromDomain(projectCode, moduleCode, item))
            .collect(Collectors.toList());
    }

    @Mapping(target = "dataViewCode", source = "id.id")
    @Mapping(target = "dataSourceName", source = "name")
    @Mapping(target = "dataViewType", source = "dataSource.type")
    ModuleGatewayVO.GatewayViewItem dataViewDtoFromDomain(DataView dataView);

    default List<ModuleGatewayVO.GatewayViewItem> dataViewDtosFromDomains(Map<String, DataView> map) {
        return map.values().stream()
            .map(ModuleGatewayVOAssembly.ASSEMBLY::dataViewDtoFromDomain)
            .collect(Collectors.toList());
    }

}
