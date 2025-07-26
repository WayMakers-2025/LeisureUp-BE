package org.leisureup.info.weather.service;

import static org.assertj.core.api.Assertions.*;

import java.time.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.leisureup.*;
import org.springframework.beans.factory.annotation.*;

class BaseDateTimeBuilderTest extends IntegrationTestSupport {

    @Autowired
    BaseDateTimeBuilder baseDateTimeBuilder;

    private static Stream<Arguments> serveData() {
        return Stream.of(
                Arguments.of(
                        TestData.of(
                                2025, 7, 26, 21, 0,
                                "20250726", "2000"
                        )
                ),
                Arguments.of(
                        TestData.of(
                                2025, 7, 26, 20, 15,
                                "20250726", "2000"
                        )
                ),
                Arguments.of(
                        TestData.of(
                                2025, 7, 26, 2, 30,
                                "20250726", "0200"
                        )
                ),
                Arguments.of(
                        TestData.of(
                                2025, 7, 26, 2, 15,
                                "20250726", "0200"
                        )
                ),
                Arguments.of(
                        TestData.of(
                                2025, 7, 26, 2, 14,
                                "20250725", "2300"
                        )
                ),
                Arguments.of(
                        TestData.of(
                                2025, 7, 26, 1, 30,
                                "20250725", "2300"
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("serveData")
    @DisplayName("주어진 시각에서 올바른 예보 기준 시각을 받는다.")
    void buildInfoFrom(TestData testData) {

        var time = testData.time();
        var expectedDate = testData.baseDate();
        var expectedTime = testData.baseTime();

        var resp = baseDateTimeBuilder.buildInfoFrom(time);

        assertThat(resp).isNotNull().hasNoNullFieldsOrProperties();
        assertThat(resp.baseDate()).isEqualTo(expectedDate);
        assertThat(resp.baseTime()).isEqualTo(expectedTime);
    }

}

@SuppressWarnings("ALL")
record TestData(
        LocalDateTime time,
        String baseDate, String baseTime
) {

    static TestData of(
            int year, int month, int day, int hour, int minute,
            String baseDate, String baseTime
    ) {
        return new TestData(TestData.of(month, day, hour, minute), baseDate, baseTime);
    }

    private static LocalDateTime of(int month, int day, int hour, int minute) {
        return LocalDateTime.of(2025, month, day, hour, minute);
    }
}