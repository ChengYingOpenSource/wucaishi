package com.cy.onepush.dataview.infrastructure.repository.mybatis.assembly;

import com.cy.onepush.dataview.domain.DataViewIdWithVersion;
import com.cy.onepush.dataview.infrastructure.repository.mybatis.params.CodeAndVersionsParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper
public interface CodeAndVersionsParamAssembly {

    CodeAndVersionsParamAssembly ASSEMBLY = Mappers.getMapper(CodeAndVersionsParamAssembly.class);

    @Mapping(target = "code", source = "dataViewIdWithVersion.dataViewId.id")
    @Mapping(target = "version", source = "dataViewIdWithVersion.version.id")
    CodeAndVersionsParam paramFromDomain(DataViewIdWithVersion dataViewIdWithVersion);

    List<CodeAndVersionsParam> paramsFromDomains(Collection<DataViewIdWithVersion> collection);

}
