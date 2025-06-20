package org.leisureup.location.spi.internal;

import java.util.*;
import lombok.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.repository.*;
import org.leisureup.location.spi.*;
import org.springframework.stereotype.*;

@Component
@RequiredArgsConstructor
public class CategorySpiImpl implements CategorySpi {

    private final CategoryRepository categoryRepo;

    @Override
    public List<CategoryInfo> getAllCategories() {
        return categoryRepo.findAll().stream()
                .map(CategorySpiUtil::toRecord)
                .toList();
    }
}

class CategorySpiUtil {

    static CategoryInfo toRecord(Category category) {
        Long id = category.getId();
        String name = category.getName();
        String recommendCode = "";
        Cat cat = resolveCategoryType(category);

        String code;
        CategoryRecommend catRecommend = category.getRecommendation();

        if (
                catRecommend != null &&
                (code = catRecommend.getRecommendingCode()) != null
        ) {
            recommendCode = code;
        }

        return new CategoryInfo(id, name, cat, recommendCode);
    }

    static Cat resolveCategoryType(Category category) {
        return switch (category.getCategoryType()) {
            case "EARTH" -> Cat.EARTH;
            case "WATER" -> Cat.WATER;
            case "SKY" -> Cat.SKY;
            default -> Cat.ETC;
        };
    }
}
