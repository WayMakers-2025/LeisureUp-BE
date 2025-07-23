package org.leisureup.global.response.external.base;

import com.fasterxml.jackson.annotation.*;
import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.response.external.*;

@Slf4j
public abstract class BaseResponse<I> implements ExternalApiResponse<I> {

    @JsonIgnore
    private final String SUCCESS_CODE;
    @Getter
    @Setter(AccessLevel.PRIVATE)
    private Response<I> response;

    protected BaseResponse(String SUCCESS_CODE) {
        this.SUCCESS_CODE = SUCCESS_CODE;
    }

    @Override
    public boolean isSuccess() {
        return response != null && response.isSuccess(SUCCESS_CODE);
    }

    @Override
    public boolean isEmpty() {
        return response.body().isEmpty();
    }

    @Override
    public String getResultMessage() {
        try {
            return response.header().resultMsg();
        } catch (Exception e) {
            log.warn("Failed to extract result message from response", e);
            return "";
        }
    }

    @Override
    public I getSingleItem() {
        return response.body().getSingleItem();
    }

    @Override
    public List<I> getItems() {
        return response.body().getItems();
    }
}
