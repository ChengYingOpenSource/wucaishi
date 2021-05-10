package com.cy.onepush.datastructure.domain;

import com.cy.onepush.common.publishlanguage.datastructure.DataStructureId;
import com.cy.onepush.common.framework.domain.AbstractAggregateRoot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class DataStructure extends AbstractAggregateRoot<String> {

    private DataType dataType = DataType.NONE;
    private String name;
    private String field;
    private boolean required = false;
    private List<DataStructure> dataStructures;

    public DataStructure(DataStructureId id) {
        super(id);
        this.dataStructures = new ArrayList<>(8);
    }

    @Override
    public DataStructureId getId() {
        return DataStructureId.of(super.getId().getId());
    }

    /**
     * validate the params by this data structure
     *
     * @param params the params to be validated
     * @return {true} success while {false} failed
     */
    public boolean validate(Object params) {
        try {
            dataType.resolve(this, params);
            return Boolean.TRUE;
        } catch (IllegalArgumentException e) {
            return Boolean.FALSE;
        }
    }

    public Object resolve(Object params) throws IllegalArgumentException {
        return dataType.resolve(this, params);
    }

}
