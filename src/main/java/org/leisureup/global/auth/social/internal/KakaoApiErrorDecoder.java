package org.leisureup.global.auth.social.internal;

public class KakaoApiErrorDecoder extends DefaultOAuthApiErrorDecoder {

    private static final String MSG_PREFIX = """
            Kakao 로그인 중 문제가 발생했습니다.
            """;

    private KakaoApiErrorDecoder() {
        super(MSG_PREFIX);
    }
}
