package org.leisureup.location.internal.dto.api;

import java.util.*;
import lombok.*;
import org.leisureup.location.internal.dto.api.internal.*;

/**
 * TourApi 응답 명시를 위한 추상 클래스
 *
 * @param <I> TourApi 응답의 {@code item} 속성 클래스
 */
@NoArgsConstructor
@AllArgsConstructor
public abstract class TourApiResponse<I> implements ApiResponse<I> {

    @Getter     // Getter 없으면 response null 로 설정되던데 왜그런거지...?
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
