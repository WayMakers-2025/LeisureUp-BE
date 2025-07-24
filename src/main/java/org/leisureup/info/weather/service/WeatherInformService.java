package org.leisureup.info.weather.service;

import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.info.spi.*;
import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;
import org.leisureup.info.weather.dto.response.*;
import org.leisureup.info.weather.service.client.*;
import org.springframework.stereotype.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherInformService {

    private final WeatherWarningApiClient warningApiClient;
    private final WeatherWarningContentSupplier contentParser;
    private final InfoSpi infoSpi;
    private final MidTermForecastApiClient midTermForecastApiClient;

    /**
     * 현재 발효된 기상 특보 내용을 조회
     */
    public WeatherWarningResponse getWeatherWarning() {

        // 특보 내용을 조회
        RawWeatherWaringContent rawContent = WeatherInformUtils.toRawContent(
                warningApiClient.getWeatherWarning()
        );

        List<SingleWeatherWarning> parsedWarnings;

        // 발효된 특보별 주의사항, 발효 지역을 추출
        try {
            String allContents = rawContent.content();

            if (allContents != null && !allContents.isEmpty()) {
                parsedWarnings = Arrays.stream(
                                rawContent.content().split("\n")
                        )
                        .map(contentParser::parseSingleContent)
                        .toList();
            } else {
                parsedWarnings = Collections.emptyList();
            }

        } catch (Exception e) {
            log.warn("Failed to parse weather warning", e);
            parsedWarnings = Collections.emptyList();
        }

        return new WeatherWarningResponse(
                parsedWarnings, rawContent
        );
    }

    /**
     * 어느 위치의 중기 육상 에보를 조회
     */
    public MidTermLandResponse getMidTermLandForecast(
            double x, double y
    ) {
        String region = infoSpi.getCodeOn(x, y, CodeType.WEATHER_LAND_FORECAST);
        return midTermForecastApiClient.forecastLand(region);
    }

    /**
     * 어느 위치의 중기 기온 에보를 조회
     */
    public MidTermTemperatureResponse getMidTermTemperatureForecast(
            double x, double y
    ) {
        String region = infoSpi.getCodeOn(x, y, CodeType.WEATHER_TEMPERATURE_FORECAST);
        return midTermForecastApiClient.forecastTemperature(region);
    }
}

class WeatherInformUtils {

    static RawWeatherWaringContent toRawContent(Warning info) {
        return new RawWeatherWaringContent(
                info.warningContent(),
                info.preWarningContent(),
                info.announcedAt(),
                info.activeAt(),
                info.sequence(),
                info.additionalInfo()
        );
    }

}