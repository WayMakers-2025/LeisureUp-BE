package org.leisureup.location.internal.service;

import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.*;
import static org.mockito.Mockito.*;

import java.time.*;
import java.util.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.leisureup.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.dto.api.*;
import org.leisureup.location.internal.dto.event.*;
import org.leisureup.location.internal.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.bean.override.mockito.*;
import org.springframework.test.context.event.*;

@Slf4j
@RecordApplicationEvents
class DataSyncEventHandlerTest extends IntegrationTestSupport {

    private static Long existingCategoryId;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ApplicationEvents applicationEvents;
    @Autowired
    DataSyncEventHandler dataSyncEventHandler;
    @Autowired
    LocationRepository locationRepo;
    @Autowired
    CategoryRepository categoryRepo;
    @Autowired
    CommonInfoCacheRepository commonInfoCacheRepo;
    @MockitoBean
    TourApiService tourApiService;

    @BeforeEach
    void setUp() {
        applicationEvents.clear();

        CatOther category = CatOther.of("other", "111");
        existingCategoryId = categoryRepo.save(category).getId();
    }

    @AfterEach
    void tearDown() {
        locationRepo.deleteAll();
        categoryRepo.deleteAll();
        commonInfoCacheRepo.deleteAll();
    }

    @Test
    @DisplayName("API 데이터가 수정되지 않은 경우, 마지막 동기화 일시를 update 하는 이벤트가 생성된다.")
    void testUpdateSyncCaseOnCheck() {

        Long testId = 10L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fewMinutesAgo = now.minusMinutes(10);
        LocalDateTime longTimeAgo = now.minusMonths(1);

        // api 상 수정되지 않은 resp 를 return 하도록 mock
        var mockResp = DataSyncEventHandlerTestUtils.genInfo(
                testId, longTimeAgo, longTimeAgo
        );
        when(tourApiService.getCommonInfo(testId))
                .thenAnswer(invocation -> {
                    Long id = invocation.getArgument(0, Long.class);

                    if (testId.equals(id)) {
                        return mockResp;
                    }

                    throw new RuntimeException();
                });

        DataSyncCheckEvent testEvent = new DataSyncCheckEvent(
                testId, fewMinutesAgo, fewMinutesAgo
        );

        dataSyncEventHandler.handleSyncCheckEvent(testEvent);

        await()
                .atMost(Duration.ofSeconds(5L))
                .pollDelay(Duration.ofMillis(100L))
                .untilAsserted(() -> {
                    List<UpdateLastSyncTimeEvent> updateSyncEvents
                            = applicationEvents.stream(UpdateLastSyncTimeEvent.class).toList();
                    List<DataSyncEvent> dataSyncEvents
                            = applicationEvents.stream(DataSyncEvent.class).toList();

                    assertThat(updateSyncEvents).hasSize(1);
                    assertThat(dataSyncEvents).isEmpty();
                });
    }

    @Test
    @DisplayName("API 데이터가 수정된 경우, 정보 동기화 이벤트가 생성된다.")
    void testDataSyncCaseOnCheck() {

        Long testId = 100L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime longTimeAgo = now.minusMonths(1);
        LocalDateTime apiModifiedTime = now.minusDays(5);

        // api 상 수정된 resp 를 return 하도록 mock
        var mockResp = DataSyncEventHandlerTestUtils.genInfo(
                testId, longTimeAgo, apiModifiedTime
        );
        when(tourApiService.getCommonInfo(testId))
                .thenAnswer(invocation -> {
                    Long id = invocation.getArgument(0, Long.class);

                    if (testId.equals(id)) {
                        return mockResp;
                    }

                    throw new RuntimeException();
                });

        DataSyncCheckEvent testEvent = new DataSyncCheckEvent(
                testId, longTimeAgo, longTimeAgo
        );

        dataSyncEventHandler.handleSyncCheckEvent(testEvent);

        await()
                .atMost(Duration.ofSeconds(5L))
                .pollDelay(Duration.ofMillis(100L))
                .untilAsserted(() -> {
                    List<UpdateLastSyncTimeEvent> updateSyncEvents
                            = applicationEvents.stream(UpdateLastSyncTimeEvent.class).toList();
                    List<DataSyncEvent> dataSyncEvents
                            = applicationEvents.stream(DataSyncEvent.class).toList();

                    assertThat(updateSyncEvents).isEmpty();
                    assertThat(dataSyncEvents).hasSize(1);
                });
    }

    @Test
    @DisplayName("동기화 일시 update 이벤트가 발생했을 때 일시가 수정된다.")
    void handleUpdateLastSyncTimeEvent() {

        Long testId = 20L;
        LocalDateTime fewMinutesAgo = LocalDateTime.now().minusMinutes(10);

        setupLocation(testId, fewMinutesAgo);

        UpdateLastSyncTimeEvent event = new UpdateLastSyncTimeEvent(testId);
        dataSyncEventHandler.handleUpdateLastSyncTimeEvent(event);

        await()
                .atMost(Duration.ofSeconds(5L))
                .pollDelay(Duration.ofMillis(100L))
                .untilAsserted(() -> {
                    Location modified = locationRepo.findById(testId)
                            .orElseThrow();

                    assertThat(modified.getLastModifiedAt()).isAfter(fewMinutesAgo);
                });
    }

    @Test
    @DisplayName("정보 동기화 이벤트가 발생했을 때 정보가 수정된다.")
    void handleDataSyncEvent() {

        Long testId = 200L;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime longTimeAgo = now.minusMonths(1);
        LocalDateTime apiModifiedTime = now.minusDays(5);

        setupLocation(testId, longTimeAgo);

        var mockResp = DataSyncEventHandlerTestUtils.genInfo(
                testId, longTimeAgo, apiModifiedTime
        );
        when(tourApiService.getCommonInfo(testId))
                .thenAnswer(invocation -> {
                    Long id = invocation.getArgument(0, Long.class);

                    if (testId.equals(id)) {
                        return mockResp;
                    }

                    throw new RuntimeException();
                });

        DataSyncEvent event = new DataSyncEvent(testId);

        dataSyncEventHandler.handleDataSyncEvent(event);

        await()
                .atMost(Duration.ofSeconds(5L))
                .pollDelay(Duration.ofMillis(100L))
                .untilAsserted(() -> {
                    Location modified = locationRepo.findById(testId)
                            .orElseThrow();

                    assertThat(modified.getLastModifiedAt()).isAfter(now.minusHours(1));

                    DataSyncEventHandlerTestUtils.assertEquality(modified, mockResp);
                });
    }

    private void setupLocation(Long id, LocalDateTime lastModifiedTime) {
        Location location = DataSyncEventHandlerTestUtils.genLocation(id, lastModifiedTime);

        location.changeCategory(categoryRepo.findById(existingCategoryId).orElseThrow());

        locationRepo.save(location);
    }
}

class DataSyncEventHandlerTestUtils {

    private static final Random RANDOM = new Random();

    static CommonInfo genInfo(
            Long locationId, LocalDateTime createdTime, LocalDateTime modifiedTime
    ) {
        return new CommonInfo(
                locationId, locationId, "title",
                createdTime, modifiedTime,
                "tel", "telName", "homepage",
                "img1", "img2",
                "cat1", "cat2", "cat3",
                "add1", "add2", "zip",
                RANDOM.nextDouble(), RANDOM.nextDouble(),
                "overview"
        );
    }

    static Location genLocation(Long locationId, LocalDateTime lastModifiedTime) {
        Location gen = Location.of(
                locationId, locationId, "first-title",
                GpsCord.of(RANDOM.nextDouble(), RANDOM.nextDouble()),
                Address.of("", "", ""),
                LocationDescription.of("", "", "",
                        "", "")
        );

        gen.synchronizeTo(lastModifiedTime);
        gen.changeModifiedTime(lastModifiedTime);

        return gen;
    }

    static void assertEquality(Location given, CommonInfo expected) {

        assertThat(given.getId()).isEqualTo(expected.contentId());
        assertThat(given.getTitle()).isEqualTo(expected.title());

        GpsCord givenCord = given.getGpsCord();
        GpsCord expectedCord = expected.gpsCord();

        assertThat(givenCord.getGpsX()).isEqualTo(expectedCord.getGpsX());
        assertThat(givenCord.getGpsY()).isEqualTo(expectedCord.getGpsY());

        Address givenAddress = given.getAddress();
        Address expectedAddress = expected.address();

        assertThat(givenAddress.getBriefedAddress())
                .isEqualTo(expectedAddress.getBriefedAddress());
        assertThat(givenAddress.getDetailedAddress())
                .isEqualTo(expectedAddress.getDetailedAddress());
        assertThat(givenAddress.getZipcode()).isEqualTo(expectedAddress.getZipcode());

        LocationDescription givenDesc = given.getDescription();
        LocationDescription expectedDesc = expected.locationDescription();

        assertThat(givenDesc.getOverview()).isEqualTo(expectedDesc.getOverview());
        assertThat(givenDesc.getHomepageInfo()).isEqualTo(expectedDesc.getHomepageInfo());
        assertThat(givenDesc.getTelephoneNumber()).isEqualTo(expectedDesc.getTelephoneNumber());
        assertThat(givenDesc.getLargeThumbnailUrl()).isEqualTo(expectedDesc.getLargeThumbnailUrl());
        assertThat(givenDesc.getSmallThumbnailUrl()).isEqualTo(expectedDesc.getSmallThumbnailUrl());
    }
}