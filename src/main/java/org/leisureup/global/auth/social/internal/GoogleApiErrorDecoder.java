package org.leisureup.global.auth.social.internal;

public class GoogleApiErrorDecoder extends DefaultOAuthApiErrorDecoder {

    private static final String MSG_PREFIX = """
            Google 로그인 중 문제가 발생했습니다.
            """;

    private GoogleApiErrorDecoder() {
        super(MSG_PREFIX);
    }
}
