package org.leisureup.location.spi;

import static org.assertj.core.api.Assertions.*;

import java.util.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.leisureup.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.repository.*;
import org.springframework.beans.factory.annotation.*;

@Slf4j
class CategorySpiTest extends IntegrationTestSupport {

    @Autowired
    CategorySpi categorySpi;

    @Autowired
    CategoryRepository categoryRepo;

    @BeforeEach
    void setUp() {
        CatEarth sample1 = CatEarth.of("1", "1");
        CatWater sample2 = CatWater.of("2", "2", "ab");
        CatOther sample3 = CatOther.of("3", "3", "cd");

        categoryRepo.saveAll(List.of(sample1, sample2, sample3));
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
}