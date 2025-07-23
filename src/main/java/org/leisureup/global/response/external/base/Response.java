package org.leisureup.global.response.external.base;

public record Response<I>(
        Header header,
        Body<I> body
) {

    private boolean nonNull() {
        return header != null && body != null;
    }

    public boolean isSuccess(String successCode) {
        return nonNull() && header.isSuccess(successCode) && body.isValid();
    }
}
