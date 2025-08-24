package org.leisureup.global.response.external.weather;

import org.leisureup.global.response.external.base.*;

public final class WeatherApiResponse<I> extends BaseResponse<I> {

    private static final String SUCCESS_CODE = "00";

    public WeatherApiResponse() {
        super(SUCCESS_CODE);
    }
}
