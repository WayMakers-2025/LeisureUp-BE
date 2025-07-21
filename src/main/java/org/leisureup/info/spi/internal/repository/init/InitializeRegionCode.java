package org.leisureup.info.spi.internal.repository.init;

import jakarta.annotation.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.info.spi.internal.domain.*;
import org.leisureup.info.spi.internal.repository.*;
import org.leisureup.info.spi.internal.repository.init.land.*;
import org.leisureup.info.spi.internal.repository.init.temperature.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

@Slf4j
@Profile("init-region-codes")
@Component
@RequiredArgsConstructor
public class InitializeRegionCode {

    private final RegionCodeInitializer initializer;

    @PostConstruct
    public void init() {
        log.info("Initializing region codes");

        try {
            initializer.initializeForLandCodes();
            initializer.initializeForTemperatureCodes();
            log.info("Region codes has been initialized");
        } catch (Exception e) {
            log.warn("Failed to initialize region codes due to {}", e.getMessage(), e);
        }
    }
}

@Slf4j
@Profile("init-region-codes")
@Component
@RequiredArgsConstructor
class RegionCodeInitializer {

    private final RegionCodeRepository regionCodeRepo;
    private final PositionRepository positionRepo;

    private static <E extends Enum<?>> List<Position> bindCodeEntityWithPosition(
            Map<String, ? extends RegionCode> codeEntityMap,
            Map<String, List<E>> codeGpsCordMap,
            BiFunction<RegionCode, E, Position> entityBinder
    ) {
        Set<String> gpsCordCode = codeGpsCordMap.keySet();

        if (!codeEntityMap.keySet().containsAll(gpsCordCode)) {
            throw new IllegalStateException("Given code set doesn't match");
        }

        List<Position> positions = new ArrayList<>();

        for (String code : gpsCordCode) {
            var entity = codeEntityMap.get(code);
            List<E> cords = codeGpsCordMap.get(code);

            positions.addAll(
                    cords.stream()
                            .map(gc -> entityBinder.apply(entity, gc))
                            .toList()
            );
        }

        return positions;
    }

    private static <K, V> Map<K, V> listToMap(Function<V, K> keyMapper, List<V> list) {
        return list.stream()
                .collect(
                        Collectors.toMap(keyMapper, Function.identity())
                );
    }

    private static <K, V> Map<K, List<V>> groupBy(Function<V, K> keyMapper, V[] values) {

        Map<K, List<V>> result = new HashMap<>();

        for (V value : values) {
            K key = keyMapper.apply(value);
            List<V> list = result.computeIfAbsent(key, k -> new ArrayList<>());
            list.add(value);
        }

        return result;
    }

    @Transactional
    void initializeForLandCodes() {

        var entities = Arrays.stream(LandCode.values())
                .map(LandCode::toEntity)
                .toList();

        regionCodeRepo.saveAll(entities);

        Map<String, LandForecastCode> codeEntityMap = listToMap(
                RegionCode::getRegionCode, entities
        );

        Map<String, List<GpsCordForLand>> codeGpsCordMap = groupBy(
                gc -> gc.getLandCode().getCode(),
                GpsCordForLand.values()
        );

        BiFunction<RegionCode, GpsCordForLand, Position> entityBinder
                = (rc, gc) -> gc.bindPositionTo(rc);

        List<Position> positions = bindCodeEntityWithPosition(
                codeEntityMap, codeGpsCordMap,
                entityBinder
        );

        positionRepo.saveAll(positions);
    }

    @Transactional
    void initializeForTemperatureCodes() {

        var entities = Arrays.stream(TemperatureCode.values())
                .map(TemperatureCode::toEntity)
                .toList();

        regionCodeRepo.saveAll(entities);

        Map<String, TemperatureForecastCode> codeEntityMap = listToMap(
                RegionCode::getRegionCode, entities
        );

        Map<String, List<GpsCordForTemperature>> codeGpsCordMap = groupBy(
                gc -> gc.getTemperatureCode().getCode(),
                GpsCordForTemperature.values()
        );

        BiFunction<RegionCode, GpsCordForTemperature, Position> entityBinder
                = (rc, gc) -> gc.bindPositionTo(rc);

        List<Position> positions = bindCodeEntityWithPosition(
                codeEntityMap, codeGpsCordMap,
                entityBinder
        );

        positionRepo.saveAll(positions);
    }
}