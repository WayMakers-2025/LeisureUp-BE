package org.leisureup.info.weather.service;

import lombok.extern.slf4j.*;
import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;

/**
 * 데이터 검증용 abstract class
 */
@Slf4j
public abstract class BaseStrategy implements ShortForecastStrategy {

    /**
     * 주어진 정보를 안전하게 넣을 수 있는지 검증한다.
     */
    protected final void assertDataInsertable(
            ShortTermForecast forecastInfo, ShortForecastInDayDto dst
    ) {
        if (forecastInfo == null || dst == null) {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }

        String forecastTime = forecastInfo.fcstTime();

        if (forecastTime == null) {
            throw new IllegalArgumentException("Forecast date and time cannot be null.");
        }

        String forecastDate = forecastInfo.getForecastDateInFormat();
        String forecastDateOnDst = dst.getForecastDate();

        if (!forecastDate.equals(forecastDateOnDst)) {
            throw new IllegalArgumentException("Forecast date must be matched.");
        }
    }

    protected final boolean isDataEmpty(ShortTermForecast forecastInfo) {
        String data = forecastInfo.fcstValue();
        return data == null || data.isEmpty();
    }

    protected final String formatDataIfPossible(
            ShortTermForecast forecastInfo, String format
    ) {
        if (isDataEmpty(forecastInfo)) {
            return "정보 없음";
        }

        String data = forecastInfo.fcstValue();

        try {
            return String.format(format, data);
        } catch (Exception e) {
            log.warn("Failed to format data due to [{}]", e.getClass().getSimpleName(), e);
            return "정보 없음";
        }
    }
}
