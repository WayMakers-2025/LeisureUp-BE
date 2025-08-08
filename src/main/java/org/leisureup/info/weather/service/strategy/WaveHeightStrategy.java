package org.leisureup.info.weather.service.strategy;

import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;
import org.leisureup.info.weather.service.*;
import org.springframework.stereotype.*;

/**
 * 파도 높이 정보를 넣어주는 전략
 */
@Component
public class WaveHeightStrategy extends BaseStrategy {

    @Override
    public void addForecastInfo(ShortTermForecast forecastInfo, ShortForecastInDayDto dst) {
        super.assertDataInsertable(forecastInfo, dst);

        String waveHeight = super.formatDataIfPossible(forecastInfo, "%sm");
        String forecastTime = forecastInfo.fcstTime();

        if (waveHeight.startsWith("-")) {
            waveHeight = "정보 없음";
        }

        dst.getOrCreateForecastInTime(forecastTime)
                .addForecast(this.getType(), waveHeight);
    }

    @Override
    public ShortForecastType getType() {
        return ShortForecastType.WAVE_HEIGHT;
    }
}
