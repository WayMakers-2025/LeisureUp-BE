package org.leisureup.location.internal.service;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.github.tomakehurst.wiremock.*;
import com.github.tomakehurst.wiremock.matching.*;
import java.nio.file.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.leisureup.location.internal.dto.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.cloud.contract.wiremock.*;
import org.springframework.test.context.*;
import org.springframework.util.*;

@SuppressWarnings("SpringBootApplicationProperties")
@SpringBootTest
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "feign.tour-api.url=http://localhost:${wiremock.server.port}",
        "tourApi.key=testing"
})
public class AdditionalInfoServiceTest {

    private static final Long leisureLocationId = 2773417L,
            hotelLocationId = 3103316L,
            restaurantLocationId = 2789499L;
    @Value("${tourApi.key}")
    String apiKeyForTest;
    @Value("${tourApi.types.os}")
    String os;
    @Value("${tourApi.types.app}")
    String app;
    @Value("${tourApi.types.response}")
    String response;
    @Autowired
    WireMockServer wireMockServer;
    @Autowired
    AdditionalInfoService additionalInfoService;

    private static byte[] supplyResponse(Long locationId) {

        String classPath = String.format(
                "classpath:" +
                "mock-server-response/tour-api/get-detailed-info" +
                "/%d.json", locationId
        );

        try {
            return Files.readAllBytes(
                    ResourceUtils.getFile(classPath).toPath()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void stubMockServer(Long locationId, Long contentTypeId) {

        byte[] body = supplyResponse(locationId);

        Map<String, StringValuePattern> queryParams = new HashMap<>();
        queryParams.put("serviceKey", equalTo(apiKeyForTest));
        queryParams.put("MobileApp", equalTo(app));
        queryParams.put("MobileOS", equalTo(os));
        queryParams.put("_type", equalTo(response));
        queryParams.put("contentId", equalTo(String.valueOf(locationId)));
        queryParams.put("contentTypeId", equalTo(String.valueOf(contentTypeId)));

        stubFor(
                get(urlPathEqualTo("/detailIntro2"))
                        .withQueryParams(queryParams)
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withBody(body)
                                        .withHeader(
                                                "Content-Type", "application/json"
                                        )
                        )
        );
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
    @DisplayName("레저 장소에 대한 추가 정보를 조회할 수 있다.")
    void getAdditionalLeisureInfo() {

        stubMockServer(leisureLocationId, LocationType.LEISURE.getContentTypeId());

        var resp = additionalInfoService.getAdditionalLeisureInfo(leisureLocationId);

        assertThat(resp).isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("숙박 장소에 대한 추가 정보를 조회할 수 있다.")
    void getAdditionalHotelInfo() {

        stubMockServer(hotelLocationId, LocationType.HOTEL.getContentTypeId());

        var resp = additionalInfoService.getAdditionalHotelInfo(hotelLocationId);

        assertThat(resp).isNotNull()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("음식점 장소에 대한 추가 정보를 조회할 수 있다.")
    void getAdditionalRestaurantInfo() {

        stubMockServer(restaurantLocationId, LocationType.RESTAURANT.getContentTypeId());

        var resp = additionalInfoService.getAdditionalRestaurantInfo(restaurantLocationId);

        assertThat(resp).isNotNull()
                .hasNoNullFieldsOrProperties();
    }
}