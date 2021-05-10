package com.cy.onepush.datasource.infrastructure.repository.mybatis.assembly;

import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.datasource.infrastructure.repository.mybatis.bean.DataSourceDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface DataSourceDOAssembly {

    DataSourceDOAssembly ASSEMBLY = Mappers.getMapper(DataSourceDOAssembly.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", source = "dataSource.id.id")
    @Mapping(target = "dsType", source = "dataSource.type")
    @Mapping(target = "conf", expression = "java( dataSource.getProperties().toString() )")
    @Mapping(target = "remark", constant = "")
    @Mapping(target = "gmtModified", source = "date")
    @Mapping(target = "gmtCreated", source = "date")
    @Mapping(target = "gmtModifier", expression = "java( -1L )")
    @Mapping(target = "gmtCreator", expression = "java( -1L )")
    DataSourceDO doFromDomain(DataSource dataSource, Date date);

    default  List<DataSourceDO> dosFromDomains(Collection<DataSource> collection, Date date) {
        return collection.stream()
            .map(item -> DataSourceDOAssembly.ASSEMBLY.doFromDomain(item , date))
            .collect(Collectors.toList());
    }

}
