package com.cy.onepush.dataview.interfaces.impl;

import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.dataview.application.DataViewApplicationService;
import com.cy.onepush.dataview.interfaces.DataViewApi;
import com.cy.onepush.dataview.interfaces.params.DebugDataViewParams;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DataViewApiImpl implements DataViewApi {

    private final DataViewApplicationService dataViewApplicationService;

    @Override
    public Result<List<String>> types() {
        return Result.<List<String>>builder().success(dataViewApplicationService.allViewTypes()).build();
    }

    @Override
    public Result<String> debug(DebugDataViewParams params) {
        final String uid = dataViewApplicationService.debug(params);
        return Result.<String>builder().data(uid).build();
    }

}
