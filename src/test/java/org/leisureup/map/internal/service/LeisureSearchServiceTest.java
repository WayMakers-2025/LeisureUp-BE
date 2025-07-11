package org.leisureup.map.internal.service;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.databind.*;
import com.github.tomakehurst.wiremock.*;
import java.util.*;
import org.junit.jupiter.api.*;
import org.leisureup.map.internal.dto.request.*;
import org.leisureup.map.internal.dto.request.SearchLeisureRequest.*;
import org.leisureup.map.internal.dto.response.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.cloud.contract.wiremock.*;
import org.springframework.test.context.*;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "feign.tour-api.url=http://localhost:${wiremock.server.port}",
        "tourApi.key=testing"
})
class LeisureSearchServiceTest {

    static final String BASE_DIR
            = "mock-server-response/tour-api/location-base-search";
    static final String BASE_INFO_FILE
            = "base-info.json";
    static final LeisureFilter filter1 = LeisureFilter.Skate,
            filter2 = LeisureFilter.Sledding,
            filter3 = LeisureFilter.Tracking;
    static JsonNode baseInfoNode;
    static CordInfo cordInfo;
    static PagingInfo pagingInfo;
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
    LeisureSearchService service;

    @BeforeAll
    static void prepare() {
        baseInfoNode = Utils.readFrom(BASE_DIR, BASE_INFO_FILE);

        double mapX = baseInfoNode.get("mapX").asDouble();
        double mapY = baseInfoNode.get("mapY").asDouble();
        int radius = baseInfoNode.get("radius").asInt();

        int pageNo = baseInfoNode.get("pageNo").asInt();
        int numOfRows = baseInfoNode.get("numOfRows").asInt();

        cordInfo = new CordInfo(mapX, mapY, radius);
        pagingInfo = new PagingInfo(pageNo, numOfRows, Sort.DIST);
    }

    private static <T> void assertBasicPageResp(MultiPageResponse<T> resp) {

        assertThat(resp).isNotNull().hasNoNullFieldsOrProperties();
        assertThat(resp.pageNo()).isEqualTo(pagingInfo.pageNo());
        assertThat(resp.pageSize()).isEqualTo(pagingInfo.pageSize());

        int givenElements = resp.numOfGivenElements();

        var elementMap = resp.elements();

        int sum = 0;
        for (String key : elementMap.keySet()) {
            var singlePageResp = elementMap.get(key);

            assertThat(singlePageResp).isNotNull();
            assertThat(singlePageResp.pageNo()).isEqualTo(pagingInfo.pageNo());
            assertThat(singlePageResp.pageSize()).isEqualTo(pagingInfo.pageSize());

            int numOfElements = singlePageResp.numOfElements();
            assertThat(singlePageResp.elements())
                    .isNotNull()
                    .hasSize(numOfElements);

            sum += numOfElements;
        }

        assertThat(givenElements).isEqualTo(sum);
    }

    @BeforeEach
    void setup() {
        wireMockServer.start();

        String case1 = filter1.getFullCode();
        String case2 = filter2.getFullCode();
        String case3 = filter3.getFullCode();
        String case4 = "any-search.json";

        stubMockServer(case1 + ".json", case1);
        stubMockServer(case2 + ".json", case2);
        stubMockServer(case3 + ".json", case3);
        stubMockServer(case4, null);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.resetAll();
        wireMockServer.stop();
    }

    @Test
    @DisplayName("레저 종류 필터링이 없으면 상위 레포츠 분류로 검색한다.")
    void searchAnyLeisure() {

        var resp = service.searchAnyLeisure(cordInfo, pagingInfo);

        // 응답이 예상되로 잘 페이징 처리 되었는지 확인
        assertBasicPageResp(resp);

        // 기본 검색에 대한 응답이 맞는지 확인
        assertThat(resp.numOfPageResponse()).isEqualTo(1);

        var elementMap = resp.elements();
        assertThat(elementMap).hasSize(1)
                .containsKey(LeisureSearchService.FILTER_NAME_ON_ANY_SEARCH);

    }

    @Test
    @DisplayName("다수의 레저 종류 필터링으로 검색할 수 있다.")
    void searchLeisureWithFilters() {

        Set<LeisureFilter> filters = Set.of(
                filter1, filter2, filter3
        );

        var resp = service.searchLeisureWithFilters(cordInfo, pagingInfo, filters);

        // 응답이 예상되로 잘 페이징 처리 되었는지 확인
        assertBasicPageResp(resp);

        // 요청에 넣은 필터 개수대로 페이징 응답이 구성되었는지 확인
        assertThat(resp.numOfPageResponse()).isEqualTo(3);

        var elementMap = resp.elements();
        assertThat(elementMap).hasSize(filters.size())
                .containsKey(filter1.name())
                .containsKey(filter2.name())
                .containsKey(filter3.name());
    }

    private void stubMockServer(
            String respFileName, String fullCategoryCode
    ) {

        double mapX = baseInfoNode.get("mapX").asDouble();
        double mapY = baseInfoNode.get("mapY").asDouble();
        int radius = baseInfoNode.get("radius").asInt();

        int pageNo = baseInfoNode.get("pageNo").asInt();
        int numOfRows = baseInfoNode.get("numOfRows").asInt();

        var queryParams = Utils.buildParamWith(
                mapX, mapY, radius, pageNo, numOfRows, "E",
                fullCategoryCode
        );
        queryParams.put("serviceKey", equalTo(apiKeyForTest));
        queryParams.put("MobileApp", equalTo(app));
        queryParams.put("MobileOS", equalTo(os));
        queryParams.put("_type", equalTo(response));

        byte[] response = Utils.supplyResponse(BASE_DIR, respFileName);

        stubFor(
                get(urlPathEqualTo("/locationBasedList2"))
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