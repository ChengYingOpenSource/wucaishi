package com.cy.onepush.datasource.interfaces.assembly;

import com.cy.onepush.datasource.domain.DataSource;
import com.cy.onepush.datasource.interfaces.vo.DataSourceVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper
public interface DataSourceVOAssembly {

    DataSourceVOAssembly ASSEMBLY = Mappers.getMapper(DataSourceVOAssembly.class);

    @Mapping(target = "code", source = "id.id")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "updateTime", expression = "java( null )")
    @Mapping(target = "createTime", expression = "java( null )")
    DataSourceVO voFromDomain(DataSource dataSource);

    List<DataSourceVO> vosFromDomains(Collection<DataSource> dataSources);

}
