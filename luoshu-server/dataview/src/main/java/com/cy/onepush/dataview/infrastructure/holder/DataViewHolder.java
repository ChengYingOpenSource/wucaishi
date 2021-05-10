package com.cy.onepush.dataview.infrastructure.holder;

import com.cy.onepush.common.publishlanguage.dataview.DataViewId;
import com.cy.onepush.dataview.domain.DataView;
import com.cy.onepush.dataview.domain.DataViewIdWithVersion;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class DataViewHolder {

    private static DataView EMPTY = new DataView(DataViewId.of("EMPTY"), "EMPTY");

    public static DataViewHolder INSTANCE = new DataViewHolder();

    private final Map<DataViewIdWithVersion, DataView> HOLDER = new ConcurrentHashMap<>(16);

    public void replaceDataView(DataView dataView) {
        HOLDER.put(DataViewIdWithVersion.of(dataView.getId(), dataView.getDataViewVersion()), dataView);
    }

    public void removeDataView(DataView dataView) {
        HOLDER.remove(dataView.getId());
    }

    public Collection<DataView> all() {
        return Collections.unmodifiableCollection(HOLDER.values());
    }

    public DataView getById(DataViewIdWithVersion dataViewIdWithVersion) {
        return HOLDER.get(dataViewIdWithVersion);
    }

    public boolean contains(DataView DataView) {
        return HOLDER.values().parallelStream().anyMatch(item -> item.getId().equals(DataView.getId()));
    }

    public boolean occupied(DataViewIdWithVersion dataViewIdWithVersion) {
        final AtomicBoolean occupied = new AtomicBoolean(false);
        HOLDER.compute(dataViewIdWithVersion, (key, old) -> {
            if (old == null) {
                occupied.set(true);
                return EMPTY;
            }

            return old;
        });

        return occupied.get();
    }

}
