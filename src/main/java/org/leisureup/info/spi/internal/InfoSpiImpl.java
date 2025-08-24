package org.leisureup.info.spi.internal;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import org.leisureup.info.spi.*;
import org.springframework.stereotype.*;

@Component
public class InfoSpiImpl implements InfoSpi {

    private final Map<CodeType, CodeTypeStrategy> strategyMap;
    private final LambertConformalConicProjector lambertProjector;

    public InfoSpiImpl(
            LambertConformalConicProjector lambertProjector,
            List<CodeTypeStrategy> strategies
    ) {
        this.lambertProjector = lambertProjector;
        this.strategyMap = strategies.stream().collect(
                Collectors.toMap(CodeTypeStrategy::getType, Function.identity())
        );
    }

    @Override
    public String getCodeOn(double x, double y, CodeType codeType) {
        return strategyMap.get(codeType).getCodeOn(x, y);
    }

    @Override
    public LambertProjectionCord convertGpsCord(double x, double y) {
        return lambertProjector.project(x, y);
    }
}
