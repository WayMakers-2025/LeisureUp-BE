package org.leisureup.global.auth.social;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.*;
import com.github.tomakehurst.wiremock.*;
import java.io.*;
import org.junit.jupiter.api.*;
import org.leisureup.global.auth.dto.api.*;
import org.leisureup.global.exception.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.cloud.contract.wiremock.*;
import org.springframework.test.context.*;

@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "feign.openid.kakao=http://localhost:${wiremock.server.port}"
})
class KakaoOAuthClientTest {

    private static final String resourceDir = "mock-server-response/oauth/kakao";
    private static final String validToken = "valid",
            invalidToken = "invalid";
    private static String sub, name;
    @Autowired
    WireMockServer wireMockServer;
    @Autowired
    ObjectMapper objMapper;
    @Autowired
    KakaoOAuthClient kakaoOAuthClient;

    @BeforeEach
    void setUp() throws IOException {
        wireMockServer.start();

        stubMockServer(401, invalidToken);
        byte[] validResponse = stubMockServer(200, validToken);

        var getUserInfoKakao = objMapper.reader()
                .readValue(validResponse, GetUserInfoKakao.class);

        sub = getUserInfoKakao.sub();
        name = getUserInfoKakao.nickname();
    }

    @AfterEach
    void tearDown() {
        wireMockServer.resetAll();
        wireMockServer.stop();
    }

    @Test
    @DisplayName("인가 token 으로 정보를 가져올 수 있다.")
    void fetchInfo() {

        OAuthResponse resp = kakaoOAuthClient.fetchInfo(validToken);

        assertThat(resp).isNotNull();

        assertThat(resp).satisfies(
                r -> assertThat(r.socialId()).isEqualTo(sub),
                r -> assertThat(r.name()).isEqualTo(name)
        );

    }

    @Test
    @DisplayName("비인가 token 은 에러를 일으킨다.")
    void testInvalidToken() {

        assertThatThrownBy(() -> kakaoOAuthClient.fetchInfo(invalidToken))
                .isInstanceOf(ClientSideException.class);

    }

    private byte[] stubMockServer(int status, String token) {
        byte[] body = Utils.supplyResponse(resourceDir, token + ".json");

        stubFor(
                get("/v1/oidc/userinfo")
                        .withHeader("Authorization", equalTo(Utils.toBearer(token)))
                        .willReturn(
                                aResponse()
                                        .withStatus(status)
                                        .withBody(body)
                                        .withHeader(
                                                "Content-Type", "application/json"
                                        )
                        )
        );

        return body;
    }
}