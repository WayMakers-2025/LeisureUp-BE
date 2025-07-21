package org.leisureup.global.response.external.base;

public record Header(
        String resultCode,
        String resultMsg
) {

    private static final String DEFAULT_SUCCESS_CODE = "0000";
    private static final String DEFAULT_SUCCESS_MSG = "OK";

    public boolean isSuccess() {
        return DEFAULT_SUCCESS_CODE.equals(resultCode) &&
               DEFAULT_SUCCESS_MSG.equals(resultMsg);
    }

    public boolean isSuccess(String successCode) {
        if (successCode == null || successCode.isEmpty()) {
            throw new IllegalArgumentException("successCode is null or empty");
        }

        return successCode.equals(resultCode);
    }
}
