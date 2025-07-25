package org.leisureup.info.weather.service.client;

import org.leisureup.global.exception.*;
import org.leisureup.global.response.external.*;

public class ForecastUtils {

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

}
