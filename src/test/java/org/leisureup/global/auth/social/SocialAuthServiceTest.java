package org.leisureup.global.auth.social;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.stream.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.leisureup.*;
import org.leisureup.global.auth.dto.request.*;
import org.leisureup.global.auth.dto.request.SignInUpRequest.*;
import org.leisureup.global.auth.token.internal.*;
import org.leisureup.member.internal.domain.*;
import org.leisureup.member.internal.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.bean.override.mockito.*;

@Slf4j
class SocialAuthServiceTest extends IntegrationTestSupport {

    private static final String preAssignedToken = "사용자는 이전에 가입했었다.";
    private static final String newSocialToken = "사용자는 현재 처음 가입중이다.";
    private static final Long kakaoSignIn = 1L, kakaoSignUp = 2L;
    private static final Long appleSignIn = 3L, appleSignUp = 4L;
    private static final Long googleSignIn = 5L, googleSignUp = 6L;
    private static Long memberId;
    @Autowired
    SocialAuthService socialAuthService;
    @Autowired
    MemberRepository memberRepo;
    @Autowired
    KakaoOAuthRepository kakaoOAuthRepo;
    @Autowired
    AppleOAuthRepository appleOAuthRepo;
    @Autowired
    GoogleOAuthRepository googleOAuthRepo;
    @Autowired
    AccessJwtProvider accessJwtProvider;
    @Autowired
    RefreshJwtProvider refreshJwtProvider;
    @MockitoSpyBean
    KakaoOAuthClient kakaoOAuthClient;
    @MockitoSpyBean
    AppleOAuthClient appleOAuthClient;
    @MockitoSpyBean
    GoogleOAuthClient googleOAuthClient;

    private static Stream<Arguments> signInRequests() {
        return Stream.of(
                Arguments.of(new SignInUpRequest(AuthType.KAKAO, preAssignedToken)),
                Arguments.of(new SignInUpRequest(AuthType.APPLE, preAssignedToken)),
                Arguments.of(new SignInUpRequest(AuthType.GOOGLE, preAssignedToken))
        );
    }

    private static Stream<Arguments> signUpRequests() {
        return Stream.of(
                Arguments.of(new SignInUpRequest(AuthType.KAKAO, newSocialToken)),
                Arguments.of(new SignInUpRequest(AuthType.APPLE, newSocialToken)),
                Arguments.of(new SignInUpRequest(AuthType.GOOGLE, newSocialToken))
        );
    }

    @BeforeEach
    void setUp() {

        setupOAuthClientMock(kakaoSignIn, kakaoSignUp, kakaoOAuthClient);
        setupOAuthClientMock(googleSignIn, googleSignUp, googleOAuthClient);
        setupOAuthClientMock(appleSignIn, appleSignUp, appleOAuthClient);

        var member = memberRepo.save(Member.of("test", "test"));
        kakaoOAuthRepo.save(KakaoOAuth.of(kakaoSignIn, member));
        appleOAuthRepo.save(AppleOAuth.of(appleSignIn, member));
        googleOAuthRepo.save(GoogleOAuth.of(googleSignIn, member));

        memberId = member.getId();
    }

    @AfterEach
    void tearDown() {
        kakaoOAuthRepo.deleteAll();
        appleOAuthRepo.deleteAll();
        googleOAuthRepo.deleteAll();
        memberRepo.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("signInRequests")
    @DisplayName("이미 가입한 유저는 소셜 인증을 통해 로그인할 수 있다.")
    void signIn(SignInUpRequest req) {
        var response = socialAuthService.signInOrSignUp(req);

        assertThat(response).isNotNull();
        assertThat(response).satisfies(
                r -> assertThat(r.id()).isNull(),
                r -> assertThat(r.accessToken()).isNotBlank(),
                r -> assertThat(r.refreshToken()).isNotBlank()
        );

        String at = response.accessToken();
        String rt = response.refreshToken();

        assertThat(accessJwtProvider.getMemberId(at)).isPresent()
                .get().isEqualTo(memberId);
        assertThat(refreshJwtProvider.getMemberId(rt)).isPresent()
                .get().isEqualTo(memberId);
    }

    @ParameterizedTest
    @MethodSource("signUpRequests")
    @DisplayName("가입하지 않은 사용자는 소셜 인증을 통해 가입할 수 있다.")
    void signUp(SignInUpRequest req) {
        log.info("start");
        var response = socialAuthService.signInOrSignUp(req);
        Long newMemberId = response.id();

        assertThat(response).isNotNull();
        assertThat(newMemberId).isNotNull();
        assertThat(response).satisfies(
                r -> assertThat(r.accessToken()).isNotBlank(),
                r -> assertThat(r.refreshToken()).isNotBlank()
        );

        String at = response.accessToken();
        String rt = response.refreshToken();

        assertThat(accessJwtProvider.getMemberId(at)).isPresent()
                .get().isEqualTo(newMemberId);
        assertThat(refreshJwtProvider.getMemberId(rt)).isPresent()
                .get().isEqualTo(newMemberId);

        assertThat(memberRepo.findById(newMemberId))
                .isPresent();
    }

    void setupOAuthClientMock(Long idOnSignIn, Long idOnSignUp, OAuthClient client) {
        when(client.fetchInfo(anyString()))
                .thenAnswer(invocation -> {
                    String token = invocation.getArgument(0, String.class);

                    if (preAssignedToken.equals(token)) {
                        return OAuthResponse.of(idOnSignIn, "signIn", "signIn");
                    } else if (newSocialToken.equals(token)) {
                        return OAuthResponse.of(idOnSignUp, "signUp", "signUp");
                    }

                    throw new RuntimeException();
                });
    }
}