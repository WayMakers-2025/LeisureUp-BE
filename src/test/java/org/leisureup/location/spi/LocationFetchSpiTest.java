package org.leisureup.location.spi;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.github.tomakehurst.wiremock.*;
import com.github.tomakehurst.wiremock.matching.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.cloud.contract.wiremock.*;
import org.springframework.test.context.*;
import org.springframework.util.*;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "feign.tour-api.url=http://localhost:${wiremock.server.port}",
        "tourApi.key=testing"
})
class LocationFetchSpiTest {

    private final static Long dbExistingLocationId = 10L;
    private final static Long apiExistingLocationId = 990140L;
    private final static Long apiNonExistentLocationId = 0L;

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
    LocationFetchSpi locationFetchSpi;
    @Autowired
    LocationRepository locationRepo;
    @Autowired
    CategoryRepository categoryRepo;

    private static byte[] supplyResponse(Long locationId) {

        String classPath = String.format(
                "classpath:" +
                "mock-server-response/tour-api/get-common-detail" +
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

    void stubMockServer(Long locationId) {

        byte[] body = supplyResponse(locationId);

        Map<String, StringValuePattern> queryParams = new HashMap<>();
        queryParams.put("serviceKey", equalTo(apiKeyForTest));
        queryParams.put("MobileApp", equalTo(app));
        queryParams.put("MobileOS", equalTo(os));
        queryParams.put("_type", equalTo(response));
        queryParams.put("contentId", equalTo(String.valueOf(locationId)));

        stubFor(
                get(urlPathEqualTo("/detailCommon2"))
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

        stubMockServer(apiExistingLocationId);
        stubMockServer(apiNonExistentLocationId);

        Category cat = CatOther.of("testing", "abcde");
        categoryRepo.save(cat);

        GpsCord cord = GpsCord.of(0.1234, 0.1234);
        Location loc = Location.of(
                dbExistingLocationId, "test", cord,
                null, null
        );
        loc.changeCategory(cat);
        loc.synchronizeTo(LocalDateTime.now());

        locationRepo.save(loc);
    }

    @AfterEach
    void tearDown() {
        locationRepo.deleteAll();
        categoryRepo.deleteAll();
        wireMockServer.resetAll();
        wireMockServer.stop();
    }

    @Test
    @DisplayName("장소가 존재하면 true 를 반환한다.")
    void fetchIfLocationExists() {

        // DB 에 장소가 있어도 true
        assertThat(locationFetchSpi.fetchIfLocationExists(dbExistingLocationId)).isTrue();

        // DB 에 장소가 없지만 API 상 있으면 true
        assertThat(locationFetchSpi.fetchIfLocationExists(apiExistingLocationId)).isTrue();

        // DB 에 자동 저장된다.
        assertThat(locationRepo.existsById(apiExistingLocationId)).isTrue();

    }

    @Test
    @DisplayName("장소가 없으면 false 를 반환한다.")
    void testNonExistingLocation() {

        assertThat(locationFetchSpi.fetchIfLocationExists(apiNonExistentLocationId)).isFalse();

    }
}