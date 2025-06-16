package org.leisureup.location.internal.dto.api.internal;

public record Header(
        String resultCode,
        String resultMsg
) {

    private static final String SUCCESS_CODE = "0000";
    private static final String SUCCESS_MSG = "OK";

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(resultCode) && SUCCESS_MSG.equals(resultMsg);
    }
}