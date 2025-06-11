package org.leisureup.global.auth.aspect;

import jakarta.servlet.http.*;
import lombok.*;
import org.aspectj.lang.annotation.*;
import org.leisureup.global.*;
import org.leisureup.global.auth.token.service.*;
import org.springframework.beans.factory.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;

@Aspect
@Component
@RequiredArgsConstructor
public class JwtAuthAspect {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER
            = HttpHeaders.AUTHORIZATION;
    private final ObjectProvider<HttpServletRequest> requestProvider;
    private final TokenAuthService tokenAuthService;
    private final AuthHolder authHolder;

    private static String removeBearerPrefix(String authHeaderVal) {

        return authHeaderVal == null || authHeaderVal.isEmpty() ? "" :
                authHeaderVal.replace(BEARER_PREFIX, "");
    }

    // 반드시 Jwt 가 필요한 곳의 PointCut
    @Pointcut("""
            @within(org.leisureup.global.JwtAuthRequired) ||
            @annotation(org.leisureup.global.JwtAuthRequired)
            """)
    public void authRequired() {
    }

    // 선택적으로 Jwt 가 필요한 곳의 PointCut
    @Pointcut("""
            @within(org.leisureup.global.JwtAuthIfPossible) ||
            @annotation(org.leisureup.global.JwtAuthIfPossible)
            """)
    public void authIfPossible() {
    }

    @Before("authRequired()")
    public void checkJwtAuth() {

        String headerVal = requestProvider.getObject()
                .getHeader(AUTHORIZATION_HEADER);

        // 헤더가 존재하지 않는다.
        if (headerVal == null) {
            throw new UnauthenticatedException();
        }

        // 토큰이 유효하지 않으면 .getMemberIdFromAt 에서 에러가 발생한다.
        String accessToken = removeBearerPrefix(headerVal);
        Long memberId = tokenAuthService.getMemberIdFromAt(accessToken);

        authHolder.setMemberId(memberId);
    }

    @Before("authIfPossible()")
    public void checkJwtAuthIfPossible() {
        try {
            checkJwtAuth();
        } catch (Exception ignored) {
            // 선택적 인가 상황으로 에러는 무시된다
        }
    }
}
