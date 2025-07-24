package org.leisureup.info.weather.service.client;

import java.time.*;
import java.time.format.*;
import java.util.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.exception.*;
import org.leisureup.global.response.external.*;
import org.leisureup.info.weather.dto.api.*;
import org.leisureup.info.weather.dto.response.*;
import org.leisureup.info.weather.dto.response.MidTermLandResponse.*;
import org.leisureup.info.weather.dto.response.MidTermTemperatureResponse.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Slf4j
@Component
public class MidTermForecastApiClient {

    private final String key, rspType;
    private final MidTermForecastApi midTermForecastApi;

    public MidTermForecastApiClient(
            MidTermForecastApi midTermForecastApi,
            @Value("${weatherApi.forecast.mid-term.key}") String key,
            @Value("${weatherApi.forecast.mid-term.type}") String rspType
    ) {
        this.midTermForecastApi = midTermForecastApi;
        this.key = key;
        this.rspType = rspType;
    }

    public MidTermLandResponse forecastLand(String landRegionCode) {

        LocalDate today = MidTermForecastApiClientUtils.getCurrentDate();

        var resp = midTermForecastApi.getLandForecast(
                key, rspType, landRegionCode,
                MidTermForecastApiClientUtils.formatToForecastDate(today), 1
        );

        MidTermForecastApiClientUtils.validateResp(resp);

        return MidTermForecastApiClientUtils.buildRespWith(
                today,
                resp.getSingleItem()
        );
    }

    public MidTermTemperatureResponse forecastTemperature(String temperatureCode) {

        LocalDate today = MidTermForecastApiClientUtils.getCurrentDate();

        var resp = midTermForecastApi.getTemperatureForecast(
                key, rspType, temperatureCode,
                MidTermForecastApiClientUtils.formatToForecastDate(today), 1
        );

        MidTermForecastApiClientUtils.validateResp(resp);

        return MidTermForecastApiClientUtils.buildRespWith(
                today,
                resp.getSingleItem()
        );
    }
}


class MidTermForecastApiClientUtils {

    private static final DateTimeFormatter formatter
            = DateTimeFormatter.ofPattern("yyyyMMdd");

    static LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    static String formatToForecastDate(LocalDate now) {
        return String.format(
                "%s0600",
                formatter.format(now)
        );
    }

    static void validateResp(ExternalApiResponse<?> resp) {

        if (resp == null) {
            throw new WeatherForecastApiException("API 통신 중 문제가 발생했습니다.");
        }

        String resultMsg = resp.getResultMessage();

        if (!resp.isSuccess()) {
            throw new WeatherForecastApiException(
                    String.format(
                            "API 통신 중 문제가 발생했습니다. : [%s]",
                            resultMsg
                    )
            );
        }

        if (resp.isEmpty()) {
            throw new WeatherForecastApiException(
                    String.format(
                            "API 통신에는 성공했으나 content 가 비어있습니다. : [%s]",
                            resultMsg
                    )
            );
        }
    }

    static MidTermLandResponse buildRespWith(LocalDate today, LandMidForecast resp) {

        LocalDate dateCounter = today.plusDays(3);

        var d4 = List.of(
                LandForecastInfo.of(Type.AM, resp.rnSt4Am(), resp.wf4Am()),
                LandForecastInfo.of(Type.PM, resp.rnSt4Pm(), resp.wf4Pm())
        );
        var d5 = List.of(
                LandForecastInfo.of(Type.AM, resp.rnSt5Am(), resp.wf5Am()),
                LandForecastInfo.of(Type.PM, resp.rnSt5Pm(), resp.wf5Pm())
        );
        var d6 = List.of(
                LandForecastInfo.of(Type.AM, resp.rnSt6Am(), resp.wf6Am()),
                LandForecastInfo.of(Type.PM, resp.rnSt6Pm(), resp.wf6Pm())
        );
        var d7 = List.of(
                LandForecastInfo.of(Type.AM, resp.rnSt7Am(), resp.wf7Am()),
                LandForecastInfo.of(Type.PM, resp.rnSt7Pm(), resp.wf7Pm())
        );
        var d8 = List.of(
                LandForecastInfo.of(Type.WHOLE_DAY, resp.rnSt8(), resp.wf8())
        );
        var d9 = List.of(
                LandForecastInfo.of(Type.WHOLE_DAY, resp.rnSt9(), resp.wf9())
        );
        var d10 = List.of(
                LandForecastInfo.of(Type.WHOLE_DAY, resp.rnSt10(), resp.wf10())
        );

        var dataList = List.of(
                d4, d5, d6, d7, d8, d9, d10
        );

        Map<LocalDate, List<LandForecastInfo>> result = new HashMap<>();

        for (var data : dataList) {
            dateCounter = dateCounter.plusDays(1);
            result.put(dateCounter, data);
        }

        return new MidTermLandResponse(result);
    }

    static MidTermTemperatureResponse buildRespWith(
            LocalDate today, TemperatureMidForecast resp
    ) {
        LocalDate dateCounter = today.plusDays(3);

        var d4 = TemperatureForecastInfo.of(resp.taMin4(), resp.taMax4());
        var d5 = TemperatureForecastInfo.of(resp.taMin5(), resp.taMax5());
        var d6 = TemperatureForecastInfo.of(resp.taMin6(), resp.taMax6());
        var d7 = TemperatureForecastInfo.of(resp.taMin7(), resp.taMax7());
        var d8 = TemperatureForecastInfo.of(resp.taMin8(), resp.taMax8());
        var d9 = TemperatureForecastInfo.of(resp.taMin9(), resp.taMax9());
        var d10 = TemperatureForecastInfo.of(resp.taMin10(), resp.taMax10());

        var dataList = List.of(
                d4, d5, d6, d7, d8, d9, d10
        );

        Map<LocalDate, TemperatureForecastInfo> result = new HashMap<>();

        for (var data : dataList) {
            dateCounter = dateCounter.plusDays(1);
            result.put(dateCounter, data);
        }

        return new MidTermTemperatureResponse(result);
    }
}
