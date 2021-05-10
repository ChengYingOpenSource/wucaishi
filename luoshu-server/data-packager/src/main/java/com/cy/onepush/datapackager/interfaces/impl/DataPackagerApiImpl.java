package com.cy.onepush.datapackager.interfaces.impl;

import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.datapackager.application.DataPackagerApplicationService;
import com.cy.onepush.datapackager.interfaces.DataPackagerApi;
import com.cy.onepush.datapackager.interfaces.params.DebugDataPackagerParams;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class DataPackagerApiImpl implements DataPackagerApi {

    private final DataPackagerApplicationService dataPackagerApplicationService;

    @Override
    public Result<List<String>> scriptTypes() {
        final List<String> executableScriptTypes = new ArrayList<>(dataPackagerApplicationService.executableScriptTypes());
        executableScriptTypes.sort(Comparator.comparing(item -> item));

        return Result.<List<String>>builder().success(executableScriptTypes).build();
    }

    @Override
    public Result<String> debug(DebugDataPackagerParams params) {
        final String traceId = dataPackagerApplicationService.debug(params);
        return Result.<String>builder()
            .data(traceId)
            .build();
    }

}
