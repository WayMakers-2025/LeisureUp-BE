package org.leisureup.info.weather.service;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.github.tomakehurst.wiremock.*;
import com.github.tomakehurst.wiremock.matching.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.cloud.contract.wiremock.*;
import org.springframework.test.context.*;

@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "feign.weather.warning=http://localhost:${wiremock.server.port}",
        "weatherApi.warning.key=testing"
})
class WeatherInformServiceTest {

    private static final String baseDir
            = "mock-server-response/weather/get-weather-warning";
    static final byte[] sample
            = Utils.supplyResponse(baseDir, "sample.json");
    static final byte[] emptySample
            = Utils.supplyResponse(baseDir, "empty-sample.json");
    @Value("${weatherApi.warning.key}")
    String apiKeyForTest;
    @Value("${weatherApi.warning.type}")
    String rspType;
    @Autowired
    WireMockServer wireMockServer;
    @Autowired
    WeatherInformService service;

    @BeforeEach
    void setUp() {
        wireMockServer.start();
    }

    @AfterEach
    void tearDown() {
        wireMockServer.resetAll();
        wireMockServer.stop();
    }

    @Test
    @DisplayName("기상 특보 내용을 parse 해서 제공한다.")
    void getWeatherWarning() {

        stubMockServerOn(sample);

        var resp = service.getWeatherWarning();

        assertThat(resp).isNotNull().hasNoNullFieldsOrProperties();

    }

    //    TODO : 기상 특보 없을때 정보 어떻게 주어지는지 확인해서 테스트 활성화 해두기
//    @Test
    @DisplayName("기상 특보가 존재하지 않아도 에러가 발생하지 않는다.")
    void testEmptyWeatherWarningResp() {

        stubMockServerOn(emptySample);

        var resp = service.getWeatherWarning();

        assertThat(resp).isNotNull().hasNoNullFieldsOrProperties();

    }

    private void stubMockServerOn(byte[] response) {

        Map<String, StringValuePattern> queryParams = new HashMap<>();
        queryParams.put("serviceKey", equalTo(apiKeyForTest));
        queryParams.put("dataType", equalTo(rspType));

        stubFor(
                get(urlPathEqualTo("/getPwnStatus"))
                        .withQueryParams(queryParams)
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody(response)
                                        .withHeader(
                                                "Content-Type", "application/json"
                                        )
                        )
        );
    }
}