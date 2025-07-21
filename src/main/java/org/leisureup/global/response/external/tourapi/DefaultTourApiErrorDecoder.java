package org.leisureup.global.response.external.tourapi;

import org.leisureup.global.response.external.*;

public class DefaultTourApiErrorDecoder extends DefaultFeignErrorDecoder {

    private static final String MSG_PREFIX = """
            TourApi 요청에 문제가 발생했습니다.
            """.trim();

    private DefaultTourApiErrorDecoder() {
        super(MSG_PREFIX);
    }
}
