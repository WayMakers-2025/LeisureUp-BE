package org.leisureup.location.internal.repository.init;

import jakarta.annotation.*;
import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.repository.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

/**
 * 카테고리 정보 초기화용 bean
 */
@Slf4j
@Profile("init-categories")
@Component
@RequiredArgsConstructor
class InitializeCategory {

    private final CategoryInitializer initializer;

    @PostConstruct
    void init() {
        log.info("Initializing categories");

        try {
            initializer.init();
            log.info("Categories has been initialized");
        } catch (Exception e) {
            log.info("Failed to initialize categories due to {}", e.getMessage(), e);
        }

    }

}

@Slf4j
@Profile("init-categories")
@Component
@RequiredArgsConstructor
class CategoryInitializer {

    private final CategoryRepository categoryRepo;

    private static List<Category> getAllCategoryEntities() {

        List<Category> all = new ArrayList<>(100);

        List<CatEarth> earths = Arrays.stream(Earth.values())
                .map(e -> CatEarth.of(e.getName(), e.getCode(), e.getRecommendCode()))
                .toList();

        List<CatWater> waters = Arrays.stream(Water.values())
                .map(e -> CatWater.of(e.getName(), e.getCode(), e.getRecommendCode()))
                .toList();

        List<CatSky> skies = Arrays.stream(Sky.values())
                .map(e -> CatSky.of(e.getName(), e.getCode(), e.getRecommendCode()))
                .toList();

        List<CatOther> others = Arrays.stream(Other.values())
                .map(e -> CatOther.of(e.getName(), e.getCode(), e.getRecommendCode()))
                .toList();

        all.addAll(earths);
        all.addAll(waters);
        all.addAll(skies);
        all.addAll(others);

        return all;
    }

    @Transactional
    void init() {

        List<Category> initialCategories = getAllCategoryEntities();

        categoryRepo.saveAll(initialCategories);
    }
}
