package org.leisureup.member.spi;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.stream.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.leisureup.*;
import org.leisureup.member.internal.domain.*;
import org.leisureup.member.internal.repository.*;
import org.mockito.invocation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.bean.override.mockito.*;

@Slf4j
class MemberSpiTest extends IntegrationTestSupport {

    private static final Long memberId = 1L;
    private static final Long validKakaoId = 1L,
            validGoogleId = 2L,
            validAppleId = 3L;
    private static final Long invalidKakaoId = 4L,
            invalidGoogleId = 5L,
            invalidAppleId = 6L;
    @Autowired
    MemberSpi memberSpi;
    @Autowired
    MemberRepository memberRepo;
    @MockitoBean
    AppleOAuthRepository appleOAuthRepo;
    @MockitoBean
    GoogleOAuthRepository googleOAuthRepo;
    @MockitoBean
    KakaoOAuthRepository kakaoOAuthRepo;

    private static Stream<Arguments> validSocialArgs() {
        return Stream.of(
                Arguments.of(SocialType.KAKAO, validKakaoId),
                Arguments.of(SocialType.GOOGLE, validGoogleId),
                Arguments.of(SocialType.APPLE, validAppleId)
        );
    }

    private static Stream<Arguments> invalidSocialArgs() {
        return Stream.of(
                Arguments.of(SocialType.KAKAO, invalidKakaoId),
                Arguments.of(SocialType.GOOGLE, invalidGoogleId),
                Arguments.of(SocialType.APPLE, invalidAppleId)
        );
    }

    private static Stream<Arguments> createNewMemberArgs() {
        return Stream.of(
                Arguments.of(SocialType.KAKAO, invalidKakaoId),
                Arguments.of(SocialType.GOOGLE, invalidGoogleId),
                Arguments.of(SocialType.APPLE, invalidAppleId)
        );
    }

    private static Optional<Long> setupMockAnswer(
            Long validId, InvocationOnMock invocation
    ) {
        Long id = invocation.getArgument(0, Long.class);
        Long result = validId.equals(id) ? memberId : null;
        return Optional.ofNullable(result);
    }

    @BeforeEach
    void setUp() {
        when(kakaoOAuthRepo.findMemberIdBySocial(anyLong()))
                .thenAnswer(invocation ->
                        setupMockAnswer(validKakaoId, invocation));

        when(googleOAuthRepo.findMemberIdBySocial(anyLong()))
                .thenAnswer(invocation ->
                        setupMockAnswer(validGoogleId, invocation));

        when(appleOAuthRepo.findMemberIdBySocial(anyLong()))
                .thenAnswer(invocation ->
                        setupMockAnswer(validAppleId, invocation));
    }

    @ParameterizedTest
    @MethodSource("validSocialArgs")
    @DisplayName("등록된 유저는 소셜인증 타입과 번호에 따라 번호를 식별할 수 있다.")
    void getMemberId(SocialType type, Long socialId) {

        Optional<Long> id = memberSpi.getMemberIdWithSocial(type, socialId);

        assertThat(id).isNotEmpty().get()
                .isEqualTo(memberId);
    }

    @ParameterizedTest
    @MethodSource("invalidSocialArgs")
    @DisplayName("등록되지 않은 유저는 번호를 식별할 수 없다.")
    void cannotGetMemberId(SocialType type, Long socialId) {

        Optional<Long> id = memberSpi.getMemberIdWithSocial(type, socialId);

        assertThat(id).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("createNewMemberArgs")
    @DisplayName("소셜 인증 타입에 따라 새로운 사용자를 생성할 수 있다.")
    void createNewMember(SocialType type, Long socialId) {

        String name = "test", email = "test@email";
        Long id = memberSpi.saveNewMember(type, socialId, name, email);

        Optional<Member> find = memberRepo.findById(id);

        assertThat(id).isNotNull();
        assertThat(find).isPresent();

        var member = find.get();
        assertThat(member).satisfies(
                m -> assertThat(m.getId()).isEqualTo(id),
                m -> assertThat(m.getNickname()).isEqualTo(name),
                m -> assertThat(m.getEmail()).isEqualTo(email)
        );

    }
}