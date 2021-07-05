package com.cy.onepush.datastructure.domain;

import com.cy.onepush.common.publishlanguage.datastructure.DataStructureId;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * the data structure factory
 *
 * @author WhatAKitty
 * @date 2020-12-12
 * @since 0.1.0
 */
public class DataStructureFactory {

    public static Optional<List<DataStructure>> createFromMap(String rootKey, Collection<Map<String, Object>> roots) {
        if (CollectionUtils.isEmpty(roots)) {
            return Optional.empty();
        }

        final DataStructure dataStructure = new DataStructure(DataStructureId.of("root"));

        roots.forEach(root -> dfs(dataStructure, rootKey, root));

        return Optional.of(dataStructure.getDataStructures());
    }

    private static void dfs(DataStructure parent, String rootKey, Map<String, Object> root) {
        if (root == null) {
            return;
        }

        // create data structure
        final DataStructure dataStructure = new DataStructure(DataStructureId.of(rootKey));
        dataStructure.setDataType(DataType.of(MapUtils.getString(root, "dataType")));
        dataStructure.setField(MapUtils.getString(root, "field"));
        dataStructure.setName(MapUtils.getString(root, "field", dataStructure.getField()));
        dataStructure.setRequired(MapUtils.getBooleanValue(root, "required"));
        parent.getDataStructures().add(dataStructure);

        List<Map<String, Object>> items = (List<Map<String, Object>>) MapUtils.getObject(root, "dataStructures");
        if (CollectionUtils.isEmpty(items)) {
            items = (List<Map<String, Object>>) MapUtils.getObject(root, "fields");
        }
        if (CollectionUtils.isNotEmpty(items)) {
            items.forEach(item -> dfs(dataStructure, dataStructure.getId().getId(), item));
        }
    }

}
