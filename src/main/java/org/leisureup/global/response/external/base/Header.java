package org.leisureup.global.response.external.base;

public record Header(
        String resultCode,
        String resultMsg
) {

    public boolean isSuccess(String successCode) {
        if (successCode == null || successCode.isEmpty()) {
            throw new IllegalArgumentException("successCode is null or empty");
        }

        return successCode.equals(resultCode);
    }
}
