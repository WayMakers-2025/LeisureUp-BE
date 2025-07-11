package org.leisureup.info.weather.dto.api.internal;

public record Header(
        String resultCode,
        String resultMsg
) {

    private static final String SUCCESS_CODE = "00";
    private static final String SUCCESS_MSG = "NORMAL_SERVICE";

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(resultCode) && SUCCESS_MSG.equals(resultMsg);
    }
}
