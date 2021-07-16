package com.cy.onepush.datastructure.infrastructure.repository.mybatis.assembly;

import com.cy.onepush.datastructure.domain.DataStructure;
import com.cy.onepush.datastructure.infrastructure.repository.mybatis.bean.DataStructureDO;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Mapper
public interface DataStructureAssembly {

    DataStructureAssembly ASSEMBLY = Mappers.getMapper(DataStructureAssembly.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", expression = "java( DataStructureAssembly.ASSEMBLY.toCode(parentCode, dataStructure) )")
    @Mapping(target = "name", source = "dataStructure.name")
    @Mapping(target = "field", source = "dataStructure.field")
    @Mapping(target = "targetCode", source = "targetCode")
    @Mapping(target = "required", source = "dataStructure.required")
    @Mapping(target = "parentCode", source = "parentCode")
    @Mapping(target = "gmtCreated", source = "date")
    @Mapping(target = "gmtModified", source = "date")
    DataStructureDO doFromDomain(DataStructure dataStructure, String parentCode, String targetCode, Date date);

    default List<DataStructureDO> dosFromDomain(DataStructure dataStructure, String targetCode, Date date) {
        final List<DataStructureDO> result = new ArrayList<>();

        dfs(targetCode, dataStructure, targetCode, date, result);

        return result;
    }

    default void dfs(String targetCode, DataStructure root, String parentCode, Date date, List<DataStructureDO> list) {
        if (root == null) {
            return;
        }

        final DataStructureDO current = DataStructureAssembly.ASSEMBLY.doFromDomain(root, parentCode, targetCode, date);
        list.add(current);

        final List<DataStructure> children = root.getDataStructures();
        if (CollectionUtils.isNotEmpty(children)) {
            children.forEach(child -> DataStructureAssembly.ASSEMBLY.dfs(targetCode, child, current.getCode(), date, list));
        }
    }

    @Named("toCode")
    default String toCode(String parentCode, DataStructure dataStructure) {
        return md5Code(String.format("%s_%s", parentCode, dataStructure.getField()));
    }

    default List<String> md5Codes(Collection<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }

        return codes.stream().map(DataStructureAssembly.ASSEMBLY::md5Code).collect(Collectors.toList());
    }

    default String md5Code(String code) {
        return DigestUtils.md5DigestAsHex(code.getBytes(StandardCharsets.UTF_8));
    }

}
