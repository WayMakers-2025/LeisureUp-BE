package org.leisureup.global.auth.token.service;

import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.global.auth.dto.*;
import org.leisureup.global.auth.token.internal.*;
import org.leisureup.global.auth.token.repository.*;
import org.leisureup.global.exception.*;
import org.leisureup.member.spi.*;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenAuthService {

    private final RefreshTokenRepository refreshTokenRepo;
    private final AccessJwtProvider atProvider;
    private final RefreshJwtProvider rtProvider;
    private final MemberSpi memberSpi;

    /**
     * Refresh token 이 유효한지 검사해 ID 를 추출한다.
     * <p>
     * 다음 상황에서 에러가 발생한다.
     * <li>토큰이 비어있거나 우리가 발급한 토큰이 아님</li>
     * <li>Redis 에 해당 토큰이 저장되어 있지 않거나 일치하지 않음</li>
     * <li>토큰은 유효하나 파싱된 ID 에 해당하는 사용자가 없음</li>
     */
    public Long getMemberIdFromRt(String refreshToken) {

        // invalid token
        Long memberId = rtProvider.getMemberId(refreshToken)
                .orElseThrow(() -> new InvalidTokenException(401));

        Optional<RefreshToken> storedRT = refreshTokenRepo.findById(memberId);

        // token or member not exists
        if (storedRT.isEmpty() || memberSpi.notExists(memberId)) {
            throw new NotFound("Not found");
        }

        // token mismatch
        if (!refreshToken.equals(storedRT.get().getToken())) {
            throw new InvalidTokenException(401);
        }

        return memberId;
    }

    /**
     * Access token 이 유효한지 검사해 ID 를 추출한다.
     * <p>
     * 다음 상황에서 에러가 발생한다.
     * <li>토큰이 비어있거나 우리가 발급한 토큰이 아님</li>
     * <li>토큰은 유효하나 파싱된 ID 에 해당하는 사용자가 없음</li>
     */
    public Long getMemberIdFromAt(String accessToken) {

        // invalid token
        Long memberId = atProvider.getMemberId(accessToken)
                .orElseThrow(() -> new InvalidTokenException(401));

        // member not exists
        if (memberSpi.notExists(memberId)) {
            throw new NotFound("Not found");
        }

        return memberId;
    }

    /**
     * ID 정보가 담긴 토큰들을 생성
     */
    public Tokens createTokens(Long memberId) {
        String at = atProvider.create(memberId);
        String rt = rtProvider.create(memberId);

        refreshTokenRepo.save(RefreshToken.of(memberId, rt));

        return Tokens.of(at, rt);
    }

    @Async
    @EventListener
    public void handleMemberRemovalEvent(MemberRemovalEvent event) {
        Long memberId = event.memberId();

        log.info("Member removal event received with ID: {}", memberId);

        refreshTokenRepo.deleteById(memberId);

        log.info("Refresh token has been removed for ID: {}", memberId);
    }
}
