package org.leisureup.global.exception;

public class TourApiException extends CustomException {

    public TourApiException(String message) {
        super(502, message);
    }
}
