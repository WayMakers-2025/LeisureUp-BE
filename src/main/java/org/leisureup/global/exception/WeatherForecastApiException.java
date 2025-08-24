package org.leisureup.global.exception;

public class WeatherForecastApiException extends CustomException {

    public WeatherForecastApiException(String message) {
        super(502, message);
    }
}
