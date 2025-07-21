package org.leisureup.info.spi.internal;

import java.util.*;
import java.util.Map.*;
import java.util.stream.*;
import lombok.*;
import org.leisureup.info.spi.*;
import org.leisureup.info.spi.internal.domain.*;
import org.leisureup.info.spi.internal.repository.*;
import org.springframework.stereotype.*;

@Component
@RequiredArgsConstructor
public class TemperatureForecastStrategy implements CodeTypeStrategy {

    private final PositionRepository positionRepo;

    @Override
    public String getCodeOn(double x, double y) {

        Map<String, List<Position>> neighbors
                = positionRepo.findCloseTemperatureForecastPositions(x, y, getPageRequest())
                .stream().collect(
                        Collectors.groupingBy(p -> p.getRegionCode().getRegionCode())
                );

        if (neighbors.isEmpty()) {
            throw new RuntimeException("No positions found");
        }

        return Stream.of(neighbors.entrySet()).flatMap(Collection::stream)
                .sorted(comparingKnn(x, y))
                .map(Entry::getKey)
                .findFirst()
                .orElseThrow();
    }

    @Override
    public CodeType getType() {
        return CodeType.WEATHER_TEMPERATURE_FORECAST;
    }
}
