package org.leisureup.info.weather.service;

import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.exception.*;
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
    private final ShortTermForecastApiClient shortTermForecastApiClient;
    private final BaseDateTimeBuilder baseDateTimeBuilder;
    private final ShortTermForecastComposer shortTermForecastComposer;

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

    /**
     * 어느 위치의 단기 예보를 조회
     */
    public List<ShortForecastInDayDto> getShortTermForecast(double x, double y) {

        // 요청에 사용할 좌표, 기준 시각 build
        var cordProjection = infoSpi.convertGpsCord(x, y);
        int nx = cordProjection.nx(), ny = cordProjection.ny();
        var forecastingTime = baseDateTimeBuilder.buildInfoFrom(
                WeatherInformUtils.getNow()
        );

        // 페이징 요청 계획 확인
        List<PagingRequestPlan> requestPlans = shortTermForecastApiClient.inspectPagingPlan(
                nx, ny, forecastingTime
        );

        // 비동기 요청
        var futureResponses = requestPlans.stream()
                .map(prp -> shortTermForecastApiClient.getShortTermForecast(
                        nx, ny, forecastingTime, prp
                )).toList();

        // 요청 join & 단일 list 로 평탄화
        List<ShortTermForecast> joinedResponses
                = WeatherInformUtils.joinAllFutureResponses(futureResponses)
                .stream()
                .flatMap(List::stream)
                .toList();

        // 모든 정보를 날짜별로 조합해 응답으로 제공한다.
        return shortTermForecastComposer.composeForecasts(joinedResponses);
    }
}

@Slf4j
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

    static LocalDateTime getNow() {
        return LocalDateTime.now();
    }

    static <T> List<T> joinAllFutureResponses(List<CompletableFuture<T>> futures) {
        try {
            return futures.stream()
                    .map(CompletableFuture::join)
                    .toList();
        } catch (CompletionException e) {
            var cause = e.getCause();
            if (cause instanceof CustomException ce) {
                throw ce;
            }

            log.error(
                    "Unexpected [CompletionException] occurred caused by [{}]",
                    cause.getClass().getSimpleName(), e
            );

            throw e;
        } catch (Exception e) {
            log.error(
                    "Unexpected exception [{}] occurred",
                    e.getClass().getSimpleName(), e
            );

            throw e;
        }
    }
}
