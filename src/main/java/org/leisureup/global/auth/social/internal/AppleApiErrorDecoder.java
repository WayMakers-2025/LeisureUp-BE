package org.leisureup.global.auth.social.internal;

import org.leisureup.global.response.external.*;

public class AppleApiErrorDecoder extends DefaultFeignErrorDecoder {

    private static final String MSG_PREFIX = """
            Google 로그인 중 문제가 발생했습니다.
            """;

    private AppleApiErrorDecoder() {
        super(MSG_PREFIX);
    }
}
