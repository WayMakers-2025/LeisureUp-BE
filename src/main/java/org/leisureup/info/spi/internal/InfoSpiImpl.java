package org.leisureup.info.spi.internal;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import org.leisureup.info.spi.*;
import org.springframework.stereotype.*;

@Component
public class InfoSpiImpl implements InfoSpi {

    private final Map<CodeType, CodeTypeStrategy> strategyMap;

    public InfoSpiImpl(List<CodeTypeStrategy> strategies) {
        this.strategyMap = strategies.stream().collect(
                Collectors.toMap(CodeTypeStrategy::getType, Function.identity())
        );
    }

    @Override
    public String getCodeOn(double x, double y, CodeType codeType) {
        return strategyMap.get(codeType).getCodeOn(x, y);
    }
}
