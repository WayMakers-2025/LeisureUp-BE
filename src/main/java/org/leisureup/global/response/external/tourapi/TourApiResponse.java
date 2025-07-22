package org.leisureup.global.response.external.tourapi;

import org.leisureup.global.response.external.base.*;

public final class TourApiResponse<I> extends BaseResponse<I> {

    private static final String SUCCESS_CODE = "0000";

    public TourApiResponse() {
        super(SUCCESS_CODE);
    }
}
