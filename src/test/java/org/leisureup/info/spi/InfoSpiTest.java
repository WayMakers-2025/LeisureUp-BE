package org.leisureup.info.spi;

import static org.assertj.core.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.leisureup.*;
import org.leisureup.info.spi.internal.domain.*;
import org.leisureup.info.spi.internal.repository.*;
import org.springframework.beans.factory.annotation.*;

@Slf4j
class InfoSpiTest extends IntegrationTestSupport {

    private static final double x = 10., y = 10.;
    @Autowired
    InfoSpi spi;
    @Autowired
    RegionCodeRepository regionCodeRepo;
    @Autowired
    PositionRepository positionRepo;

    private static Stream<Arguments> getCodeOnTypes() {
        return Arrays.stream(CodeType.values())
                .map(Arguments::of);
    }

    @BeforeEach
    void setUp() {

        var land = regionCodeRepo.save(
                LandForecastCode.of("land", "land")
        );

        var temperature = regionCodeRepo.save(
                TemperatureForecastCode.of("temperature", "temperature")
        );

        Position pos1 = Position.of(x, y);
        Position pos2 = Position.of(x, y);

        pos1.changeRegionTo(land);
        pos2.changeRegionTo(temperature);

        positionRepo.save(pos1);
        positionRepo.save(pos2);
    }

    @AfterEach
    void tearDown() {
        positionRepo.deleteAll();
        regionCodeRepo.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("getCodeOnTypes")
    @DisplayName("임의의 좌표에서 분류 코드 타입에 따른 코드를 제공받을 수 있다.")
    void getCodeOn(CodeType type) {
        String code = spi.getCodeOn(x, y, type);

        assertThat(code).isNotBlank();

        assertThat(regionCodeRepo.findByRegionCode(code)).isPresent();
    }
}