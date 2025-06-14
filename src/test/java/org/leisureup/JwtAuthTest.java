package org.leisureup;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.*;
import org.leisureup.global.auth.token.internal.*;
import org.leisureup.member.spi.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.*;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.*;
import org.springframework.test.web.servlet.*;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthTest {

    private static final Long memberId = 1L;
    private static final String AUTH_HEADER =
            HttpHeaders.AUTHORIZATION;
    private static String accessToken;
    @Autowired
    MockMvc mvc;
    @Autowired
    AccessJwtProvider atProvider;
    @MockitoBean
    MemberSpi memberSpi;

    @BeforeEach
    void setUp() {
        accessToken = atProvider.create(memberId);
        when(memberSpi.notExists(anyLong()))
                .thenAnswer(invocation -> {
                    Long id = invocation.getArgument(0, Long.class);
                    return !memberId.equals(id);
                });
    }

    @Test
    @DisplayName("토큰이 존재하면 정상 응답을 받는다.")
    void testAuthenticate() throws Exception {

        mvc.perform(get("/test/auth-required")
                        .header(
                                AUTH_HEADER,
                                "Bearer " + accessToken
                        )
                )
                .andExpect(status().is(200))
                .andExpect(
                        jsonPath("$.data")
                                .value(String.valueOf(memberId))
                );
    }

    @Test
    @DisplayName("토큰이 없으면 비인가 에러 응답을 받는다.")
    void testUnauthenticate() throws Exception {
        mvc.perform(get("/test/auth-required"))
                .andExpect(status().is(401));
    }

    @Test
    @DisplayName("토큰이 없어도 처리할 수 있는 응답이 있다.")
    void testPartialAuthenticate() throws Exception {
        mvc.perform(get("/test/auth-bypassable")
                        .header(
                                AUTH_HEADER,
                                "Bearer " + accessToken
                        )
                )
                .andExpect(status().is(200))
                .andExpect(
                        jsonPath("$.data")
                                .value(String.valueOf(memberId))
                );

        mvc.perform(get("/test/auth-bypassable"))
                .andExpect(status().is(200))
                .andExpect(
                        jsonPath("$.data").isEmpty()
                );
    }
}
