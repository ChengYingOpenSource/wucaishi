package com.cy.onepush.datastructure.infrastructure.repository;

import com.cy.onepush.common.publishlanguage.datastructure.DataStructureId;
import com.cy.onepush.datastructure.domain.DataStructure;
import com.cy.onepush.datastructure.domain.DataStructureRepository;
import com.cy.onepush.datastructure.domain.DataType;
import com.cy.onepush.datastructure.infrastructure.repository.mybatis.assembly.DataStructureAssembly;
import com.cy.onepush.datastructure.infrastructure.repository.mybatis.bean.DataStructureDO;
import com.cy.onepush.datastructure.infrastructure.repository.mybatis.mapper.DataStructureMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class RdsDataStructureRepository implements DataStructureRepository {

    private final DataStructureMapper dataStructureMapper;

    @Override
    public Optional<List<DataStructure>> getByTargetCode(String targetCode) {
        final List<DataStructureDO> dataStructureDOS = dataStructureMapper.selectByTargetCode(targetCode);
        if (CollectionUtils.isEmpty(dataStructureDOS)) {
            return Optional.empty();
        }

        final List<DataStructure> roots = createTree(targetCode, dataStructureDOS);
        if (CollectionUtils.isEmpty(roots)) {
            log.warn("the target coded {} has no data structure", targetCode);
            return Optional.empty();
        }

        return Optional.of(roots);
    }

    @Override
    public void add(String targetCode, DataStructure dataStructure) {
        final List<DataStructureDO> dataStructureDOS = DataStructureAssembly.ASSEMBLY.dosFromDomain(dataStructure, targetCode, new Date());

        dataStructureMapper.batchInsert(dataStructureDOS);
    }

    @Override
    public void batchAdd(Map<String, Collection<DataStructure>> map) {
        final List<DataStructureDO> collect = map.entrySet().stream()
            .map(item -> item.getValue().parallelStream()
                .map(dataStructure -> DataStructureAssembly.ASSEMBLY.dosFromDomain(dataStructure, item.getKey(), new Date()))
                .collect(Collectors.toList())
            )
            .flatMap(Collection::stream)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        dataStructureMapper.batchInsert(collect);
    }

    @Override
    public void batchInsertOrUpdate(Map<String, Collection<DataStructure>> map) {
        final List<DataStructureDO> collect = map.entrySet().stream()
            .map(item -> item.getValue().parallelStream()
                .map(dataStructure -> DataStructureAssembly.ASSEMBLY.dosFromDomain(dataStructure, item.getKey(), new Date()))
                .collect(Collectors.toList())
            )
            .flatMap(Collection::stream)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        dataStructureMapper.batchInsertOrUpdate(collect);
    }

    @Override
    public void batchDeleteByTargetCode(Collection<String> targetCodes) {
        dataStructureMapper.batchDeleteByTargetCode(targetCodes);
    }

    private List<DataStructure> createTree(String targetCode, List<DataStructureDO> list) {
        final List<DataStructure> root = new ArrayList<>();

        final Map<String, DataStructure> structureMap = list.stream()
            .map(item -> {
                final DataStructure dataStructure = new DataStructure(DataStructureId.of(item.getCode()));
                dataStructure.setDataType(DataType.of(item.getDataType()));
                dataStructure.setName(item.getName());
                dataStructure.setField(item.getField());
                dataStructure.setRequired(item.getRequired());

                return dataStructure;
            })
            .collect(Collectors.toMap(item -> item.getId().getId(), item -> item, (a, b) -> a));

        for (DataStructureDO current : list) {
            final DataStructure dataStructure = structureMap.get(current.getCode());
            if (StringUtils.isBlank(current.getParentCode())) {
                // add this into root list
                root.add(dataStructure);
                continue;
            }

            final DataStructure parentDataStructure = structureMap.get(current.getParentCode());
            if (parentDataStructure == null) {
                // add this into root list
                root.add(dataStructure);
                continue;
            }

            parentDataStructure.getDataStructures().add(dataStructure);
        }

        return root;

    }

}
