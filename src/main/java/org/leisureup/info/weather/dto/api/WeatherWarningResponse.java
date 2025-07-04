package org.leisureup.info.weather.dto.api;

import java.util.*;
import lombok.*;
import org.leisureup.info.weather.dto.api.internal.*;

@NoArgsConstructor
@AllArgsConstructor
public abstract class WeatherWarningResponse<I> implements ApiResponse<I> {

    @Getter
    private Response<I> response;

    public final String getMessage() {
        String msg = "";
        try {
            msg = response.header().resultMsg();
        } catch (Exception ignored) {
            // ignored
        }

        return msg;
    }

    @Override
    public final boolean isSuccess() {
        return response != null && response.isSuccess();
    }

    @Override
    public final boolean isEmpty() {
        if (this.isSuccess()) {
            assert response != null;
            Body<I> body = response.body();
            return body == null || body.isEmpty();
        }

        return true;
    }

    @Override
    public final I getSingleItem() {
        if (!this.isSuccess()) {
            throw new IllegalStateException("Response is not successful");
        }

        Body<I> body = response.body();
        assert body != null;

        return body.getSingleItem();
    }

    @Override
    public final List<I> getAllItems() {
        if (!this.isSuccess()) {
            throw new IllegalStateException("Response is not successful");
        }

        Body<I> body = response.body();
        assert body != null;

        return body.getAllItems();
    }
}
