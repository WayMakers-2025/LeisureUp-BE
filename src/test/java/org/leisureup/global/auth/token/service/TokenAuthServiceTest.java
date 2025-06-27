package org.leisureup.global.auth.token.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.leisureup.*;
import org.leisureup.global.auth.token.internal.*;
import org.leisureup.global.auth.token.repository.*;
import org.leisureup.global.exception.*;
import org.leisureup.member.spi.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.bean.override.mockito.*;

@Slf4j
class TokenAuthServiceTest extends IntegrationTestSupport {

    private static final Long existMemberId1 = 1L,
            existMemberId2 = 2L,
            notExistMemberId = 3L;
    private static final String invalidToken = "invalid";
    private static String validAT, validRT;
    private static String invalidMemberIdAt, invalidMemberIdRt;
    private static String validRtButNotRegistered;
    @Autowired
    TokenAuthService tokenAuthService;
    @Autowired
    AccessJwtProvider atProvider;
    @Autowired
    RefreshJwtProvider rtProvider;
    @MockitoBean
    RefreshTokenRepository refreshTokenRepo;
    @MockitoBean
    MemberSpi memberSpi;

    @BeforeEach
    void setUp() {

        validAT = atProvider.create(existMemberId1);
        validRT = rtProvider.create(existMemberId1);
        validRtButNotRegistered = rtProvider.create(existMemberId2);

        invalidMemberIdAt = atProvider.create(notExistMemberId);
        invalidMemberIdRt = rtProvider.create(notExistMemberId);

        when(refreshTokenRepo.findById(anyLong()))
                .thenAnswer(invocation -> {
                    Long id = invocation.getArgument(0, Long.class);

                    RefreshToken result = !existMemberId1.equals(id) && !existMemberId2.equals(id) ?
                            null : RefreshToken.of(id, validRT);

                    return Optional.ofNullable(result);
                });

        when(memberSpi.notExists(anyLong()))
                .thenAnswer(invocation -> {
                    Long id = invocation.getArgument(0, Long.class);
                    return !existMemberId1.equals(id) && !existMemberId2.equals(id);
                });
    }

    @Test
    @DisplayName("Refresh token 을 통해 사용자를 식별할 수 있다.")
    void getMemberIdFromRt() {
        Long result = tokenAuthService.getMemberIdFromRt(validRT);

        assertThat(result).isEqualTo(existMemberId1);
    }

    @Test
    @DisplayName("Access token 을 통해 사용자를 식별할 수 있다.")
    void getMemberIdFromAt() {
        Long result = tokenAuthService.getMemberIdFromAt(validAT);

        assertThat(result).isEqualTo(existMemberId1);
    }

    @Test
    @DisplayName("유효하지 않은 토큰은 에러를 발생시킨다.")
    void invalidTokenTest() {
        assertThatThrownBy(() -> tokenAuthService.getMemberIdFromRt(invalidToken))
                .isInstanceOf(InvalidTokenException.class);

        assertThatThrownBy(() -> tokenAuthService.getMemberIdFromAt(invalidToken))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("토큰이 유효하더라도 사용자가 식별되지 않으면 에러를 발생시킨다.")
    void memberNotExistsTest() {
        assertThatThrownBy(() -> tokenAuthService.getMemberIdFromRt(invalidMemberIdRt))
                .isInstanceOf(NotFound.class);

        assertThatThrownBy(() -> tokenAuthService.getMemberIdFromAt(invalidMemberIdAt))
                .isInstanceOf(NotFound.class);
    }

    @Test
    @DisplayName("저장되지 않은 RT 는 에러를 발생시킨다.")
    void notRegisteredRefreshTokenTest() {
        assertThatThrownBy(() -> tokenAuthService.getMemberIdFromRt(validRtButNotRegistered))
                .isInstanceOf(InvalidTokenException.class);
    }
}