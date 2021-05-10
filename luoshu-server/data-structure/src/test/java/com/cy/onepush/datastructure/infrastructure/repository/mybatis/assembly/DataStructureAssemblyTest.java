package com.cy.onepush.datastructure.infrastructure.repository.mybatis.assembly;

import com.cy.onepush.common.publishlanguage.datastructure.DataStructureId;
import com.cy.onepush.datastructure.domain.DataStructure;
import com.cy.onepush.datastructure.infrastructure.repository.mybatis.bean.DataStructureDO;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DataStructureAssemblyTest {

    @Test
    public void test_parent_code() {
        DataStructure a_1 = new DataStructure(DataStructureId.of("a_1"));
        a_1.setField("a_1");
        a_1.setName("a_1");

        DataStructure dataStructure = new DataStructure(DataStructureId.of("test"));
        dataStructure.setName("a");
        dataStructure.setField("a");
        dataStructure.getDataStructures().add(a_1);

        final List<DataStructureDO> list = DataStructureAssembly.ASSEMBLY.dosFromDomain(dataStructure, "test", new Date());
        list.sort(Comparator.comparing(DataStructureDO::getField));

        Assert.assertEquals(2, list.size());
        Assert.assertEquals("test_a", list.get(0).getCode());
        Assert.assertEquals("test_a_a_1", list.get(1).getCode());
    }

}