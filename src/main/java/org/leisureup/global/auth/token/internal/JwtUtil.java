package org.leisureup.global.auth.token.internal;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.*;
import java.time.*;
import java.util.*;
import javax.crypto.*;
import lombok.extern.slf4j.*;

@Slf4j
public class JwtUtil {

    // jwt secret key
    private final SecretKey key;

    // 만료기간 초단위
    private final long expiration;

    protected JwtUtil(String key, long expiration) {
        this.key = Keys.hmacShaKeyFor(key.getBytes());
        this.expiration = expiration;
    }

    // 주어진 걸로 토큰 생성해
    protected final String create(Map<String, Object> subjects) {
        Claims claims = Util.buildClaims(subjects, expiration);

        return Jwts.builder()
                .claims(claims)
                .signWith(key)
                .compact();
    }

    // 토큰에서 값 가져와
    protected final <T> Optional<T> getSubject(
            String token, String subjectKey, Class<T> subjectValueClazz
    ) {
        T subject = null;

        try {
            Optional<Claims> optional = Util.getClaims(key, token);

            if (optional.isPresent()) {
                subject = optional.get().get(subjectKey, subjectValueClazz);

                if (subject == null) {
                    log.warn("Subject key exists in token, but value is empty.");
                }
            } else {
                log.warn("Invalid token given. Cannot extract subject.");
            }

        } catch (RequiredTypeException e) {
            log.warn(
                    "Incompatible value clazz given. Subject cannot be supplied."
            );
        } catch (Exception e) {
            log.error(
                    "Unexpected error occurred while parsing jwt : {}",
                    e.getClass().getSimpleName(),
                    e
            );
        }

        return Optional.ofNullable(subject);
    }
}

@Slf4j
class Util {

    static Date now() {
        return Date.from(Instant.now());
    }

    static Date after(long expiration) {
        return Date.from(Instant.now().plusSeconds(expiration));
    }

    static Claims buildClaims(
            Map<String, ?> subjects, long expiration
    ) {
        return Jwts.claims()
                .add(subjects)
                .issuedAt(Util.now())
                .expiration(Util.after(expiration))
                .build();
    }

    static Optional<Claims> getClaims(
            SecretKey key, String token
    ) {
        Claims claims = null;
        try {
            claims = Jwts.parser().verifyWith(key).build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.warn(
                    "Failed to parse JWT token : {}",
                    e.getClass().getSimpleName(), e
            );
        }

        return Optional.ofNullable(claims);
    }
}