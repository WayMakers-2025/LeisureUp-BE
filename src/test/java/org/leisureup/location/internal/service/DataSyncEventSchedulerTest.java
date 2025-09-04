package org.leisureup.location.internal.service;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import java.time.*;
import java.util.*;
import java.util.stream.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.leisureup.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.dto.event.*;
import org.leisureup.location.internal.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.bean.override.mockito.*;
import org.springframework.test.context.event.*;

@Slf4j
@RecordApplicationEvents
class DataSyncEventSchedulerTest extends IntegrationTestSupport {

    private static final Random RANDOM = new Random();
    private static final List<Long> testIds = LongStream.rangeClosed(1, 10)
            .boxed().toList();
    private static List<Long> checkCandidateIds;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    ApplicationEvents applicationEvents;
    @Autowired
    DataSyncEventScheduler dataSyncEventScheduler;
    @Autowired
    LocationRepository locationRepo;
    @Autowired
    CategoryRepository categoryRepo;
    @MockitoBean
    DataSyncEventHandler eventHandler;

    private static Location genEntity(Long id) {
        String title = String.valueOf(id);
        GpsCord cord = GpsCord.of(RANDOM.nextDouble(), RANDOM.nextDouble());

        Location loc = Location.of(
                id, 32L, title, cord, null, null
        );

        loc.synchronizeTo(LocalDateTime.now());

        return loc;
    }

    @BeforeEach
    void setUp() {
        applicationEvents.clear();

        Category sampleCat = categoryRepo.save(
                CatOther.of("sample", "1234")
        );

        List<Location> locations = testIds.stream()
                .map(DataSyncEventSchedulerTest::genEntity)
                .toList();

        locations.forEach(l -> l.changeCategory(sampleCat));

        List<Location> candidates = locations.stream().limit(testIds.size() / 2).toList();
        candidates.forEach(l -> l.changeModifiedTime(LocalDateTime.now().minusMonths(2)));

        locationRepo.saveAll(locations);

        checkCandidateIds = candidates.stream().map(Location::getId).sorted().toList();
    }

    @AfterEach
    void tearDown() {
        locationRepo.deleteAll();
        categoryRepo.deleteAll();
    }

    @Test
    @DisplayName("1 개월 이내 동기화 되지 않은 장소들은 동기화 검사 이벤트가 발생한다.")
    void publishCheckEvents() {

        dataSyncEventScheduler.publishCheckEvents();

        // extract published events
        List<DataSyncCheckEvent> publishedEvents = applicationEvents
                .stream(DataSyncCheckEvent.class)
                .toList();

        assertThat(publishedEvents).hasSize(checkCandidateIds.size());

        for (DataSyncCheckEvent event : publishedEvents) {

            Long id = event.locationId();
            LocalDateTime lastModifiedTime = event.lastModifiedAt();

            assertThat(id).isNotNull().isIn(checkCandidateIds);
            assertThat(lastModifiedTime).isNotNull().isBefore(
                    LocalDateTime.now().minusWeeks(2)
            );
        }
    }
}