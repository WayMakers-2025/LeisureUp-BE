package org.leisureup.info.weather.service;

import static org.assertj.core.api.Assertions.*;
import static org.leisureup.info.weather.dto.api.ShortTermForecast.Category.*;

import java.util.*;
import java.util.stream.*;
import org.junit.jupiter.api.*;
import org.leisureup.*;
import org.leisureup.info.weather.dto.api.*;
import org.leisureup.info.weather.dto.api.ShortTermForecast.*;
import org.leisureup.info.weather.dto.response.*;
import org.springframework.beans.factory.annotation.*;

class ShortTermForecastComposerTest extends IntegrationTestSupport {

    private static final String day1 = "20250726";
    private static final List<ShortTermForecast> day1Forecasts = List.of(
            gen(POP, day1, "2200", "10"),
            gen(PTY, day1, "2200", "0"),
            gen(REH, day1, "2200", "10"),
            gen(SKY, day1, "2200", "1"),
            gen(TMX, day1, "1500", "10"),
            gen(TMN, day1, "0600", "10")
    );
    private static final String day2 = "20250727";
    private static final List<ShortTermForecast> day2Forecasts = List.of(
            gen(POP, day2, "1100", "10"),
            gen(PTY, day2, "1100", "0"),
            gen(REH, day2, "1100", "10"),
            gen(SKY, day2, "1100", "1"),
            // 다른 시각
            gen(POP, day2, "1400", "10"),
            gen(PTY, day2, "1400", "0"),
            gen(REH, day2, "1400", "10"),
            gen(SKY, day2, "1400", "1"),

            gen(TMX, day2, "1500", "10"),
            gen(TMN, day2, "0600", "10")
    );
    private static final String day3 = "20250728";
    private static final List<ShortTermForecast> day3Forecasts = List.of(
            gen(POP, day3, "0200", "10"),
            gen(PTY, day3, "0200", "0"),
            gen(REH, day3, "0200", "10"),
            gen(SKY, day3, "0200", "1")
    );
    @Autowired
    ShortTermForecastComposer composer;

    private static ShortTermForecast gen(
            Category category,
            String fcstDate,
            String fcstTime,
            String fcstValue
    ) {
        return new ShortTermForecast(category, fcstDate, fcstTime, fcstValue, 0, 0);
    }

    private static String formatForecastDate(String forecastDate) {
        return String.format(
                "%s-%s-%s",
                forecastDate.substring(0, 4), forecastDate.substring(4, 6),
                forecastDate.substring(6, 8)
        );
    }

    private static ShotTermForecastResponse getRespOnDate(
            List<ShotTermForecastResponse> forecasts, String forecastDate
    ) {
        String formatted = formatForecastDate(forecastDate);
        return forecasts.stream()
                .filter(fr -> fr.forecastDate().equals(formatted))
                .findFirst()
                .orElseThrow();
    }

    @Test
    @DisplayName("다수의 단기 예보 정보를 조합해 제공할 수 있다.")
    void composeForecasts() {

        var forecasts = Stream.of(day3Forecasts, day2Forecasts, day1Forecasts)
                .flatMap(Collection::stream)
                .toList();

        var resp = composer.composeForecasts(forecasts);

        assertThat(resp).isNotNull().hasSize(3);
        for (var r : resp) {
            assertThat(r.forecastDate()).isNotNull().isNotBlank();

            var data = r.data();
            assertThat(data).isNotNull().isNotEmpty();

            for (var d : data) {
                assertThat(d).isNotNull().hasNoNullFieldsOrProperties();
            }
        }

        var day1Resp = getRespOnDate(resp, day1);
        assertThat(day1Resp.forecastDate()).isEqualTo(formatForecastDate(day1));
        assertThat(day1Resp.maxTemperature()).isNotNull();
        assertThat(day1Resp.minTemperature()).isNotNull();
        assertThat(day1Resp.data()).hasSize(1);

        var day2Resp = getRespOnDate(resp, day2);
        assertThat(day2Resp.forecastDate()).isEqualTo(formatForecastDate(day2));
        assertThat(day2Resp.maxTemperature()).isNotNull();
        assertThat(day2Resp.minTemperature()).isNotNull();
        assertThat(day2Resp.data()).hasSize(2);

        var day3Resp = getRespOnDate(resp, day3);
        assertThat(day3Resp.forecastDate()).isEqualTo(formatForecastDate(day3));
        assertThat(day3Resp.maxTemperature()).isNull();
        assertThat(day3Resp.minTemperature()).isNull();
        assertThat(day1Resp.data()).hasSize(1);

    }

    @Test
    @DisplayName("관심사로 지정되지 않은 정보는 포함되지 않는다.")
    void testOutOfInterests() {

        var forecasts = List.of(
                gen(UUU, day1, "2200", "10"),
                gen(VVV, day1, "2200", "10")
        );

        var resp = composer.composeForecasts(forecasts);

        assertThat(resp).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("중복된 정보가 제공되면 에러를 일으킨다.")
    void testDuplicateData() {

        var forecasts = Stream.of(day2Forecasts, day1Forecasts, List.of(day1Forecasts.getFirst()))
                .flatMap(Collection::stream)
                .toList();

        assertThatThrownBy(() -> composer.composeForecasts(forecasts))
                .isInstanceOf(IllegalArgumentException.class);

    }
}
