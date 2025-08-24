package org.leisureup.info.weather.service;

import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;

/**
 * 단기 예보 정보의 종류에 따라 정보를 꾸며줄 계약
 */
public interface ShortForecastStrategy {

    /**
     * 주어진 단기 예보 정보를 {@code dst} 에 넣어준다.
     *
     * @param forecastInfo 단기 예보 정보
     * @param dst          정보를 넣을 목적지
     */
    void addForecastInfo(
            ShortTermForecast forecastInfo,
            ShortForecastInDayDto dst
    );

    /**
     * 꾸며줄 수 있는 단기 예보 종류
     */
    ShortForecastType getType();
}
