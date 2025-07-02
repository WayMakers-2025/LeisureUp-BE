package org.leisureup.info.weather.dto.api.internal;

public record Response<I>(
        Header header,
        Body<I> body
) {

    public boolean isSuccess() {
        return header != null && body != null && header.isSuccess();
    }
}
