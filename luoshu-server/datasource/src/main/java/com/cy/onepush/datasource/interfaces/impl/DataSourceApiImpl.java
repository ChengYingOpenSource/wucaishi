package com.cy.onepush.datasource.interfaces.impl;

import com.cy.onepush.common.framework.infrastructure.web.Result;
import com.cy.onepush.datasource.application.DataSourceApplicationService;
import com.cy.onepush.datasource.interfaces.DataSourceApi;
import com.cy.onepush.datasource.interfaces.params.ValidateDataSourceParams;
import com.cy.onepush.datasource.interfaces.vo.DataSourceValidateVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DataSourceApiImpl implements DataSourceApi {

    private final DataSourceApplicationService dataSourceApplicationService;

    @Override
    public Result<List<String>> types() {
        return Result.<List<String>>builder()
            .data(dataSourceApplicationService.allTypes())
            .build();
    }

    @Override
    public Result<DataSourceValidateVO> validate(String datasourceCode, ValidateDataSourceParams params) {
        final boolean result = dataSourceApplicationService.validate(params);

        final DataSourceValidateVO dataSourceValidateVO = new DataSourceValidateVO();
        dataSourceValidateVO.setMsg(null);
        dataSourceValidateVO.setOk(result);

        return Result.<DataSourceValidateVO>builder()
            .data(dataSourceValidateVO)
            .build();
    }

}
