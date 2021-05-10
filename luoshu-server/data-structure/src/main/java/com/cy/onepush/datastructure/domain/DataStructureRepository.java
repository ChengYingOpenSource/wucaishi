package com.cy.onepush.datastructure.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DataStructureRepository {

    Optional<List<DataStructure>> getByTargetCode(String targetCode);

    void add(String targetCode, DataStructure dataStructure);

    void batchAdd(Map<String, Collection<DataStructure>> map);

    void batchInsertOrUpdate(Map<String, Collection<DataStructure>> map);

    void batchDeleteByTargetCode(Collection<String> targetCodes);

}
