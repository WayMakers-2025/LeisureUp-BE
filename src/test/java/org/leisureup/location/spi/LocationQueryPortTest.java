package org.leisureup.location.spi;

import static org.assertj.core.api.Assertions.*;

import java.time.*;
import java.util.*;
import java.util.stream.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.leisureup.*;
import org.leisureup.global.exception.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.repository.*;
import org.springframework.beans.factory.annotation.*;

@Slf4j
class LocationQueryPortTest extends IntegrationTestSupport {

    private static final Random random = new Random();
    private static final List<Long> testIds = LongStream.rangeClosed(1, 10)
            .boxed().toList();
    private static Long exsitingCategoryId;
    @Autowired
    LocationQueryPort port;
    @Autowired
    LocationRepository locationRepo;
    @Autowired
    CategoryRepository categoryRepo;

    private static Location genEntity(Long id) {
        String title = String.valueOf(id);
        GpsCord cord = GpsCord.of(random.nextDouble(), random.nextDouble());

        Location loc = Location.of(
                id, title, cord, null, null
        );

        loc.synchronizeTo(LocalDateTime.now());

        return loc;
    }

    @BeforeEach
    void setUp() {

        Category sampleCat = CatOther.of("sample", "1234");
        exsitingCategoryId = categoryRepo.save(sampleCat)
                .getId();

        List<Location> locations = testIds.stream()
                .map(LocationQueryPortTest::genEntity)
                .toList();

        locations.forEach(l -> l.changeCategory(sampleCat));
        locationRepo.saveAll(locations);
    }

    @AfterEach
    void tearDown() {
        locationRepo.deleteAll();
        categoryRepo.deleteAll();
    }

    @Test
    @DisplayName("ID 목록을 통해 장소 정보를 조회할 수 있다.")
    void getLocationListById() {

        List<LocationResponse> resp = port.getLocationListById(testIds);

        assertThat(resp).isNotNull().hasSize(testIds.size());
        for (var single : resp) {
            assertThat(single).hasNoNullFieldsOrProperties();
        }
    }

    @Test
    @DisplayName("ID 목록 중 식별할 수 없는 장소가 존재하면 에러를 일으킨다.")
    void missingIdExists() {
        Long missingId1 = 12345L;
        Long missingId2 = 54321L;
        List<Long> ids = new ArrayList<>(testIds);
        ids.add(missingId1);
        ids.add(missingId2);

        assertThatThrownBy(() -> port.getLocationListById(ids))
                .isInstanceOf(NotFound.class)
                .hasMessageContainingAll(
                        String.valueOf(missingId1), String.valueOf(missingId2)
                );

    }

    @Test
    @DisplayName("랜덤한 장소를 제공받을 수 있다.")
    void getAnyLocations() {

        int max = testIds.size() / 2;
        List<LocationResponse> resp = port.getAnyLocations(max);

        assertThat(resp).isNotNull();

    }

    @Test
    @DisplayName("특정 카테고리에 속한 랜덤한 장소를 제공받을 수 있다.")
    void getAnyLocationsOnCategory() {

        int max = testIds.size() / 2;
        List<LocationResponse> resp = port.getAnyLocationsOnCategory(max,
                List.of(exsitingCategoryId)
        );

        assertThat(resp).isNotNull();

        // 없는 카테고리 ID 가 주어지면 빈 list
        Long nonExistingCategoryId = Long.MAX_VALUE;
        resp = port.getAnyLocationsOnCategory(max,
                List.of(nonExistingCategoryId)
        );

        assertThat(resp).isNotNull().isEmpty();
    }
}