package org.leisureup.info.weather.service;

import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.dto.api.*;
import org.leisureup.info.weather.dto.response.*;
import org.leisureup.info.weather.dto.response.WeatherWarningResponse;
import org.springframework.stereotype.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherInformService {

    private final WeatherWarningApiClient warningApiClient;
    private final WeatherWarningContentSupplier contentParser;

    public WeatherWarningResponse getWeatherWarning() {
        RawWeatherWaringContent rawContent = WeatherInformUtils.toRawContent(
                warningApiClient.getWeatherWarning()
        );

        List<SingleWeatherWarning> parsedWarnings;

        try {
            String allContents = rawContent.content();

            if (allContents != null && !allContents.isEmpty()) {
                parsedWarnings = Arrays.stream(rawContent.content().split("\n"))
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