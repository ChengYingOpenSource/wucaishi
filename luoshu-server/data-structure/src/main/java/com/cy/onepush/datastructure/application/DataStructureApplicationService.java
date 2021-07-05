package com.cy.onepush.datastructure.application;

import com.cy.onepush.datastructure.domain.DataType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataStructureApplicationService {

    public List<String> dataTypes() {
        return Arrays.stream(DataType.values())
            .filter(DataType::isVisible)
            .sorted(Comparator.comparingInt(DataType::getSort))
            .map(Enum::name)
            .collect(Collectors.toList());
    }

}
