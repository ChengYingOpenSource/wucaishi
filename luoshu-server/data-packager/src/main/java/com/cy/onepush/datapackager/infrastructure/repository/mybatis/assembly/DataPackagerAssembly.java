package com.cy.onepush.datapackager.infrastructure.repository.mybatis.assembly;

import com.cy.onepush.datapackager.domain.DataPackager;
import com.cy.onepush.datapackager.infrastructure.repository.mybatis.bean.DataPackagerDO;
import com.cy.onepush.datapackager.infrastructure.repository.mybatis.bean.DataPackagerScriptDO;
import com.cy.onepush.datapackager.infrastructure.repository.mybatis.bean.DataPackagerViewDO;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * data packager assembly
 *
 * @author WhatAKitty
 * @date 2020-12-13
 * @since 0.1.0
 */
@Mapper
public interface DataPackagerAssembly {

    DataPackagerAssembly ASSEMBLY = Mappers.getMapper(DataPackagerAssembly.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", source = "dataPackager.id.id")
    @Mapping(target = "name", source = "dataPackager.name")
    @Mapping(target = "reqDatastructureCode", source = "dataPackager.requestDataStructure.id.id", qualifiedByName = "md5FromDataPackager")
    @Mapping(target = "respDatastructureCode", source = "dataPackager.responseDataStructure.id.id", qualifiedByName = "md5FromDataPackager")
    @Mapping(target = "version", source = "dataPackager.version.id")
    @Mapping(target = "gmtCreated", source = "date")
    @Mapping(target = "gmtModified", source = "date")
    @Mapping(target = "gmtCreator", expression = "java( -1L )")
    @Mapping(target = "gmtModifier", expression = "java( -1L )")
    DataPackagerDO doFromDomain(DataPackager dataPackager, Date date);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataPackagerCode", source = "dataPackager.id.id")
    @Mapping(target = "version", source = "dataPackager.version.id")
    @Mapping(target = "scriptType", source = "dataPackager.executionScript.type")
    @Mapping(target = "scriptContent", source = "dataPackager.executionScript.content")
    @Mapping(target = "gmtCreated", source = "date")
    @Mapping(target = "gmtModified", source = "date")
    DataPackagerScriptDO scriptDoFromDomain(DataPackager dataPackager, Date date);

    default List<DataPackagerViewDO> viewDosFromDomain(DataPackager dataPackager, Date date) {
        if (dataPackager == null) {
            return Lists.newArrayListWithCapacity(0);
        }

        final String id = dataPackager.getId().getId();
        return dataPackager.getDataViews().entrySet().stream()
            .map(entry -> {
                final DataPackagerViewDO dataPackagerViewDO = new DataPackagerViewDO();
                dataPackagerViewDO.setDataPackagerCode(id);
                dataPackagerViewDO.setVersion(dataPackager.getVersion().getId());
                dataPackagerViewDO.setDataViewAlias(entry.getKey());
                dataPackagerViewDO.setDataViewCode(entry.getValue().getId().getId());
                dataPackagerViewDO.setGmtCreated(date);
                dataPackagerViewDO.setGmtModified(date);
                return dataPackagerViewDO;
            })
            .collect(Collectors.toList());
    }

    @Named("md5FromDataPackager")
    default String md5FromDataPackager(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }

        return DigestUtils.md5DigestAsHex(id.getBytes(StandardCharsets.UTF_8));
    }

}
