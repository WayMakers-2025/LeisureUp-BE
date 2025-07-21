package org.leisureup.location.spi;

import static org.assertj.core.api.Assertions.*;

import java.util.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.leisureup.*;
import org.leisureup.global.exception.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.repository.*;
import org.springframework.beans.factory.annotation.*;

@Slf4j
class CategorySpiTest extends IntegrationTestSupport {

    @Autowired
    CategorySpi categorySpi;

    @Autowired
    CategoryRepository categoryRepo;

    private static final Long nonExistingId = Long.MAX_VALUE;
    private static Long id1, id2, id3;

    @BeforeEach
    void setUp() {
        CatEarth sample1 = CatEarth.of("1", "1");
        CatWater sample2 = CatWater.of("2", "2", "ab");
        CatOther sample3 = CatOther.of("3", "3", "cd");

        categoryRepo.saveAll(List.of(sample1, sample2, sample3));

        id1 = sample1.getId();
        id2 = sample2.getId();
        id3 = sample3.getId();
    }

    @AfterEach
    void tearDown() {
        categoryRepo.deleteAll();
    }

    @Test
    @DisplayName("모든 카테고리 정보를 조회할 수 있다.")
    void getAllCategories() {

        List<CategoryInfo> resp = categorySpi.getAllCategories();

        assertThat(resp).hasSize(3)
                .doesNotContainNull();

        for (var one : resp) {
            assertThat(one).hasNoNullFieldsOrProperties();
        }
    }

    @Test
    @DisplayName("특정 카테고리 정보를 조회할 수 있다.")
    void getCategoryDetail() {

        for (Long id : new Long[]{id1, id2, id3}) {
            var resp = categorySpi.getCategoryDetail(id);

            assertThat(resp).isNotNull().hasNoNullFieldsOrProperties();
        }

    }

    @Test
    @DisplayName("조회할 카테고리가 없을땐 에러가 발생한다.")
    void testNotFound() {
        assertThatThrownBy(() -> categorySpi.getCategoryDetail(nonExistingId))
                .isInstanceOf(NotFound.class);
    }

    @Test
    @DisplayName("랜덤한 카테고리를 제공받을 수 있다.")
    void getAnyCategories() {

        int max = 3;    // id1, 2, 3 --> max 3
        List<CategoryInfo> resp = categorySpi.getAnyCategories(max);

        assertThat(resp).isNotNull();
        for (var one : resp) {
            assertThat(one).hasNoNullFieldsOrProperties();
        }
    }
}