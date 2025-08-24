package org.leisureup.global.auth.token.repository;

import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.redis.core.*;

@Getter
@RedisHash(value = "refresh-token", timeToLive = 360000)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    private Long memberId;
    private String token;

    private RefreshToken(Long memberId, String token) {
        this.memberId = memberId;
        this.token = token;
    }

    public static RefreshToken of(Long memberId, String token) {
        return new RefreshToken(memberId, token);
    }
}
