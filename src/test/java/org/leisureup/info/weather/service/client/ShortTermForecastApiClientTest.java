package org.leisureup.info.weather.service.client;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.github.tomakehurst.wiremock.*;
import com.github.tomakehurst.wiremock.matching.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.leisureup.info.weather.dto.*;
import org.leisureup.info.weather.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.cloud.contract.wiremock.*;
import org.springframework.test.context.*;

@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "feign.weather.forecast.short-term=http://localhost:${wiremock.server.port}",
        "weatherApi.forecast.short-term.key=testing"
})
class ShortTermForecastApiClientTest {

    private static final String baseDir
            = "mock-server-response/weather/forecast/short-term";
    private static final byte[] pagingPlan1
            = Utils.supplyResponse(baseDir, "forecast-paging-plan-1.json");
    private static final byte[] pagingPlan2
            = Utils.supplyResponse(baseDir, "forecast-paging-plan-2.json");
    private static final CordData cord1 = new CordData(1, 1);
    private static final CordData cord2 = new CordData(2, 2);
    private static final ShortTermBaseDateTimeInfo baseDateTimeInfo
            = new ShortTermBaseDateTimeInfo(null, null);
    @Value("${weatherApi.forecast.short-term.key}")
    String key;
    @Value("${weatherApi.forecast.short-term.type}")
    String rspType;
    @Autowired
    WireMockServer wireMockServer;
    @Autowired
    ShortTermForecastApiClient apiClient;

    private void stubPagingPlanResp() {

        Map<String, StringValuePattern> qp1 = buildQueryParamsFrom(cord1);
        qp1.put("pageNo", equalTo(String.valueOf(1)));
        qp1.put("numOfRows", equalTo(String.valueOf(1)));

        stubFor(
                get(urlPathEqualTo("/getVilageFcst"))
                        .withQueryParams(qp1)
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody(pagingPlan1)
                                        .withHeader("Content-Type", "application/json")
                        )
        );

        Map<String, StringValuePattern> qp2 = buildQueryParamsFrom(cord2);
        qp2.put("pageNo", equalTo(String.valueOf(1)));
        qp2.put("numOfRows", equalTo(String.valueOf(1)));

        stubFor(
                get(urlPathEqualTo("/getVilageFcst"))
                        .withQueryParams(qp2)
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody(pagingPlan2)
                                        .withHeader("Content-Type", "application/json")
                        )
        );
    }

    private Map<String, StringValuePattern> buildQueryParamsFrom(CordData cordData) {
        Map<String, StringValuePattern> queryParams = new HashMap<>();
        queryParams.put("serviceKey", equalTo(key));
        queryParams.put("dataType", equalTo(rspType));
        queryParams.put("nx", equalTo(String.valueOf(cordData.nx())));
        queryParams.put("ny", equalTo(String.valueOf(cordData.ny())));

        return queryParams;
    }

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
    @DisplayName("응답 전체 개수에 따라 올바른 페이징 계획을 세울 수 있다.")
    void inspectPagingPlan() {

        stubPagingPlanResp();

        var pagingPlan1 = apiClient.inspectPagingPlan(
                cord1.nx(), cord1.ny(), baseDateTimeInfo
        );

        assertThat(pagingPlan1).isNotNull().hasSize(7);
        for (int i = 1; i <= pagingPlan1.size(); i++) {
            var plan = pagingPlan1.get(i - 1);

            assertThat(plan.pageNo()).isEqualTo(i);
        }

        var pagingPlan2 = apiClient.inspectPagingPlan(
                cord2.nx(), cord2.ny(), baseDateTimeInfo
        );

        assertThat(pagingPlan2).isNotNull().hasSize(1);
        assertThat(pagingPlan2.getFirst().pageNo()).isEqualTo(1);
    }

}

record CordData(
        int nx, int ny
) {

}