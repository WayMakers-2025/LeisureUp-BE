package org.leisureup.location.spi.internal;

import java.util.*;
import lombok.*;
import org.leisureup.global.exception.*;
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

    @Override
    public DetailedCategoryInfo getCategoryDetail(Long categoryId) {

        Category find = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new NotFound("Category not found"));

        return CategorySpiUtil.toDetailedRecord(find);
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

    static DetailedCategoryInfo toDetailedRecord(Category category) {
        CategoryInfo basicInfo = toRecord(category);

        Long id = basicInfo.id();
        String name = basicInfo.name();
        Cat cat = basicInfo.category();
        String recommendCode = basicInfo.recommendingCode();

        String categoryCode = category.getCategoryCode();

        String briefInfo = "", target = "", requiredGear = "", warning = "", thumbnail = "";
        AdditionalCategoryInfo additionalInfo = category.getAdditionalInfo();

        if (additionalInfo != null) {
            briefInfo = additionalInfo.getBriefInfo();
            target = additionalInfo.getCategoryTarget();
            requiredGear = additionalInfo.getRequiredGear();
            warning = additionalInfo.getWarning();
            thumbnail = additionalInfo.getThumbnailUrl();
        }

        return new DetailedCategoryInfo(
                id, emptyIfNull(categoryCode), name, cat,
                emptyIfNull(briefInfo), emptyIfNull(target),
                emptyIfNull(requiredGear), emptyIfNull(warning),
                emptyIfNull(thumbnail), recommendCode
        );
    }

    private static Cat resolveCategoryType(Category category) {
        return switch (category.getCategoryType()) {
            case "EARTH" -> Cat.EARTH;
            case "WATER" -> Cat.WATER;
            case "SKY" -> Cat.SKY;
            default -> Cat.ETC;
        };
    }

    private static String emptyIfNull(String s) {
        return s == null ? "" : s;
    }
}
