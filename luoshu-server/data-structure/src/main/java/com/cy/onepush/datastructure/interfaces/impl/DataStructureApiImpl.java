package com.cy.onepush.datastructure.interfaces.impl;

import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.datastructure.application.DataStructureApplicationService;
import com.cy.onepush.datastructure.interfaces.DataStructureApi;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DataStructureApiImpl implements DataStructureApi {

    private final DataStructureApplicationService dataStructureApplicationService;

    @Override
    public Result<List<String>> types() {
        return Result.<List<String>>builder().success(dataStructureApplicationService.dataTypes()).build();
    }

}
