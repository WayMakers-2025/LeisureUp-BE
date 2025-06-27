package org.leisureup.global.auth.token.internal;

import java.util.*;

public abstract class AbstractJwtProvider extends JwtUtil {

    private static final String MEMBER_ID_SUBJECT_KEY
            = "MEMBER_ID";

    protected AbstractJwtProvider(String key, long expiration) {
        super(key, expiration);
    }

    /**
     * ID 정보가 담긴 Jwt 를 생성
     */
    public final String create(Long memberId) {
        Map<String, Object> payload = new HashMap<>();
        payload.put(MEMBER_ID_SUBJECT_KEY, memberId);

        return super.create(payload);
    }

    /**
     * Jwt 에서 ID 정보를 추출
     */
    public final Optional<Long> getMemberId(String token) {
        return super.getSubject(token, MEMBER_ID_SUBJECT_KEY, Long.class);
    }
}
