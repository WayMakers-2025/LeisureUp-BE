package org.leisureup.info.category.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;
import org.junit.jupiter.api.*;
import org.leisureup.*;
import org.leisureup.location.spi.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.test.context.bean.override.mockito.*;

class CategoryServiceTest extends IntegrationTestSupport {

    private static final int TOTAL_NUM = 5 * 3;
    private static final int NUM_EARTHS = TOTAL_NUM / 3,
            NUM_WATERS = TOTAL_NUM / 3, NUM_SKIES = TOTAL_NUM / 3;
    @Autowired
    CategoryService service;
    @MockitoBean
    CategorySpi categorySpi;

    private static CategoryInfo gen(Long id, Cat cat) {
        String name = String.format("Cat:%d", id);
        return new CategoryInfo(
                id, name, "",
                cat, name, Set.of(Season.ANY)
        );
    }

    @BeforeEach
    void setUp() {
        List<CategoryInfo> infos = new ArrayList<>(TOTAL_NUM);

        long id = 1;
        for (int i = 0; i < NUM_EARTHS; i++) {
            infos.add(gen(id, Cat.EARTH));
        }
        for (int i = 0; i < NUM_WATERS; i++) {
            infos.add(gen(id, Cat.WATER));
        }
        for (int i = 0; i < NUM_SKIES; i++) {
            infos.add(gen(id, Cat.SKY));
        }

        when(categorySpi.getAllCategories()).thenReturn(infos);
    }

    @Test
    @DisplayName("모든 카테고리 정보를 조회할 수 있다.")
    void getAllCategories() {

        var resp = service.getAllCategories();

        assertThat(resp).isNotNull().hasNoNullFieldsOrProperties();
        var earths = resp.earth();
        var waters = resp.water();
        var skies = resp.sky();

        assertThat(earths).hasSize(NUM_EARTHS);
        assertThat(waters).hasSize(NUM_WATERS);
        assertThat(skies).hasSize(NUM_SKIES);

        for (var list : new List[]{earths, waters, skies}) {
            for (var info : list) {
                assertThat(info).isNotNull().hasNoNullFieldsOrProperties();
            }
        }
    }
}