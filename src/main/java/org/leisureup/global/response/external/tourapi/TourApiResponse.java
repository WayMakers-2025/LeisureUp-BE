package org.leisureup.global.response.external.tourapi;

import java.util.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.response.external.*;
import org.leisureup.global.response.external.base.*;

@Slf4j
public record TourApiResponse<I>(
        Response<I> response
) implements ExternalApiResponse<I> {

    @Override
    public boolean isSuccess() {
        return response != null && response.isSuccess();
    }

    public boolean isSuccess(String successCode) {
        return response != null && response.isSuccess(successCode);
    }

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
