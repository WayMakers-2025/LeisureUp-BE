package org.leisureup.global.response;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private final boolean success;
    private final int code;
    private final T data;
    private final String message;

    public static <T> ApiResponse<T> success(int code, T data) {
        return new ApiResponse<>(true, code, data, null);
    }

    public static <T> ApiResponse<T> failure(int code, String message) {
        return new ApiResponse<>(false, code, null, message);
    }

    public static <T> ApiResponse<T> failure(int code, String message, T data) {
        return new ApiResponse<>(false, code, data, message);
    }
}
