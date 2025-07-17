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
    @DisplayName("ID 목록을 통해 카테고리 정보를 조회할 수 있다.")
    void getCategoryList() {

        List<Long> ids = List.of(id1, id2, id3);

        var resp = categorySpi.getCategoryList(ids);

        assertThat(resp).isNotNull().hasSize(ids.size());

        // 주어진 ID 순서대로 제공된다.
        for (int i = 0; i < ids.size(); i++) {
            Long givenId = ids.get(i);
            Long respId = resp.get(i).id();

            assertThat(givenId).isEqualTo(respId);
        }

    }

    @Test
    @DisplayName("카테고리가 없을땐 에러가 발생한다.")
    void testNotFound() {
        assertThatThrownBy(() -> categorySpi.getCategoryDetail(nonExistingId))
                .isInstanceOf(NotFound.class);

        List<Long> ids = List.of(id1, id2, id3, nonExistingId);

        assertThatThrownBy(() -> categorySpi.getCategoryList(ids))
                .isInstanceOf(NotFound.class)
                .hasMessageContaining(String.valueOf(nonExistingId));
    }
}