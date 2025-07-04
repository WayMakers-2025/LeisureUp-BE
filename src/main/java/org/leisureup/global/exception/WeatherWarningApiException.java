package org.leisureup.global.exception;

public class WeatherWarningApiException extends CustomException {

    public WeatherWarningApiException(String message) {
        super(502, message);
    }
}
