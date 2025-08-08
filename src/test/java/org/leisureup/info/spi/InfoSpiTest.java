package org.leisureup.info.spi;

import static org.assertj.core.api.Assertions.*;

import java.util.*;
import java.util.stream.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.leisureup.*;
import org.leisureup.global.exception.*;
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

    private static Stream<Arguments> serveData() {
        return Stream.of(
                Arguments.of(TestData.of(
                        126.9382, 35.1515111111111, 60, 74
                )),
                Arguments.of(TestData.of(
                        129.363544444444, 35.5796888888888, 103, 85
                )),
                Arguments.of(TestData.of(
                        127.321388888888, 35.1481333333333, 67, 74
                )),
                Arguments.of(TestData.of(
                        126.7604, 37.5591666666667, 56, 127
                ))
        );
    }

    @ParameterizedTest
    @MethodSource("serveData")
    @DisplayName("임의의 좌표를 람베르트 정각원추도법으로 변환할 수 있다.")
    void convertGpsCord(TestData data) {

        double x = data.x(), y = data.y();

        var resp = spi.convertGpsCord(x, y);

        assertThat(resp).isNotNull().hasNoNullFieldsOrProperties();
        assertThat(resp).satisfies(
                r -> assertThat(r.nx()).isEqualTo(data.nx()),
                r -> assertThat(r.ny()).isEqualTo(data.ny()),
                r -> assertThat(r.x()).isEqualTo(data.x()),
                r -> assertThat(r.y()).isEqualTo(data.y())
        );
    }

    @Test
    @DisplayName("대한민국을 벗어난 좌표를 변환할 때 에러가 발생한다.")
    void testInvalidGpsCord() {

        assertThatThrownBy(() -> spi.convertGpsCord(x, y))
                .isInstanceOf(LambertProjectorException.class);

    }
}

record TestData(
        double x, double y,
        int nx, int ny
) {

    static TestData of(double x, double y, int nx, int ny) {
        return new TestData(x, y, nx, ny);
    }
}