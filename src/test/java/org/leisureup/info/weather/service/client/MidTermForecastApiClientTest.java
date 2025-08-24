package org.leisureup.info.weather.service.client;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.github.tomakehurst.wiremock.*;
import com.github.tomakehurst.wiremock.matching.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.leisureup.info.weather.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.cloud.contract.wiremock.*;
import org.springframework.test.context.*;

@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "feign.weather.forecast.mid-term=http://localhost:${wiremock.server.port}",
        "weatherApi.forecast.mid-term.key=testing"
})
class MidTermForecastApiClientTest {

    private static final String baseDir
            = "mock-server-response/weather/forecast/mid-term";
    private static final byte[] landRespSample
            = Utils.supplyResponse(baseDir, "midterm-land-forecast.json");
    private static final byte[] temperatureRespSample
            = Utils.supplyResponse(baseDir, "midterm-temperature-forecast.json");
    private static final String landRegion = "land";
    private static final String temperatureRegion = "temp";
    @Value("${weatherApi.forecast.mid-term.key}")
    String key;
    @Value("${weatherApi.forecast.mid-term.type}")
    String rspType;
    @Autowired
    WireMockServer wireMockServer;
    @Autowired
    MidTermForecastApiClient forecastApiClient;

    @BeforeEach
    void setUp() {
        wireMockServer.start();
    }

    private Map<String, StringValuePattern> buildQueryParam(String region) {
        Map<String, StringValuePattern> queryParams = new HashMap<>();

        queryParams.put("serviceKey", equalTo(key));
        queryParams.put("dataType", equalTo(rspType));
        queryParams.put("numOfRows", equalTo(String.valueOf(1)));
        queryParams.put("regId", equalTo(region));

        return queryParams;
    }

    @AfterEach
    void tearDown() {
        wireMockServer.resetAll();
        wireMockServer.stop();
    }

    @Test
    @DisplayName("중기 육상 예보를 조회할 수 있다.")
    void forecastLand() {

        stubFor(
                get(urlPathEqualTo("/getMidLandFcst"))
                        .withQueryParams(buildQueryParam(landRegion))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody(landRespSample)
                                        .withHeader("Content-Type", "application/json")
                        )
        );

        var resp = forecastApiClient.forecastLand(landRegion);

        assertThat(resp).isNotNull();

        var mappedData = resp.landForecasts();
        assertThat(mappedData).hasSize(7);

        for (var dataList : mappedData.values()) {
            assertThat(dataList).isNotNull().isNotEmpty();
            dataList.forEach(
                    data -> assertThat(data)
                            .isNotNull().hasNoNullFieldsOrProperties()
            );
        }
    }

    @Test
    @DisplayName("중기 기온 에보를 조회할 수 있다.")
    void forecastTemperature() {

        stubFor(
                get(urlPathEqualTo("/getMidTa"))
                        .withQueryParams(buildQueryParam(temperatureRegion))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody(temperatureRespSample)
                                        .withHeader("Content-Type", "application/json")
                        )
        );

        var resp = forecastApiClient.forecastTemperature(temperatureRegion);

        assertThat(resp).isNotNull();

        var mappedData = resp.temperatureForecasts();
        assertThat(mappedData).hasSize(7);

        for (var data : mappedData.values()) {
            assertThat(data).isNotNull().hasNoNullFieldsOrProperties();
        }
    }
}