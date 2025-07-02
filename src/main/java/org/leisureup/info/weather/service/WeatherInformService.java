package org.leisureup.info.weather.service;

import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.info.weather.dto.api.*;
import org.leisureup.info.weather.dto.response.*;
import org.springframework.stereotype.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherInformService {

    private final WeatherWarningApiClient warningApiClient;

    public WarningResponse getWeatherWarning() {
        Warning info = warningApiClient.getWeatherWarning();
        return WeatherInformUtils.toResponse(info);
    }
}

class WeatherInformUtils {

    static WarningResponse toResponse(Warning info) {
        return new WarningResponse(
                info.warningContent(),
                info.preWarningContent(),
                info.announcedAt(),
                info.activeAt(),
                info.sequence(),
                info.additionalInfo()
        );
    }

}