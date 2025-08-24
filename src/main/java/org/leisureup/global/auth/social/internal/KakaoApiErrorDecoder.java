package org.leisureup.global.auth.social.internal;

import org.leisureup.global.response.external.*;

public class KakaoApiErrorDecoder extends DefaultFeignErrorDecoder {

    private static final String MSG_PREFIX = """
            Kakao 로그인 중 문제가 발생했습니다.
            """;

    private KakaoApiErrorDecoder() {
        super(MSG_PREFIX);
    }
}
