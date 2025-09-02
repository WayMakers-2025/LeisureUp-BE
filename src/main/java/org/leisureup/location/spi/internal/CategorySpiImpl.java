package org.leisureup.location.spi.internal;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;
import lombok.*;
import org.leisureup.global.exception.*;
import org.leisureup.global.logging.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.location.internal.domain.AdditionalCategoryInfo.*;
import org.leisureup.location.internal.repository.*;
import org.leisureup.location.spi.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.*;

@Component
@LogMethodIO
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
    public List<CategoryInfo> getCategoryList(List<Long> categoryIds) {

        List<Category> categories = categoryRepo.findAllByCategoryId(categoryIds);

        if (categories.size() != categoryIds.size()) {
            throw SpiUtils.throwNotFoundWithMissingIds(categoryIds, categories, Category::getId);
        }

        Map<Long, CategoryInfo> unorderedResp = categories.stream()
                .map(CategorySpiUtil::toRecord)
                .collect(Collectors.toMap(CategoryInfo::id, Function.identity()));

        return categoryIds.stream()
                .map(unorderedResp::get)
                .toList();
    }

    @Override
    public DetailedCategoryInfo getCategoryDetail(Long categoryId) {

        Category find = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new NotFound("Category not found"));

        return CategorySpiUtil.toDetailedRecord(find);
    }

    @Override
    public List<CategoryInfo> getAnyCategories(int maxElements) {

        long cnt = categoryRepo.count();

        if (cnt == 0) {
            return Collections.emptyList();
        }

        List<Category> categories = categoryRepo.findAllBy(
                CategorySpiUtil.randomPageRequest(maxElements, cnt)
        );

        return categories.stream()
                .map(CategorySpiUtil::toRecord)
                .toList();
    }
}

class CategorySpiUtil {

    private static final Random RAND = ThreadLocalRandom.current();

    static CategoryInfo toRecord(Category category) {
        Long id = category.getId();
        String name = category.getName();
        String categoryCode = category.getCategoryCode();
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

        AdditionalCategoryInfo additionalInfo = category.getAdditionalInfo();
        String thumbnailUrl = additionalInfo != null ?
                additionalInfo.getThumbnailUrl() : "";
        Set<Season> suitableSeasons;

        if (additionalInfo != null && additionalInfo.getSuitableSeasons() != null) {
            suitableSeasons = additionalInfo.getSuitableSeasons()
                    .stream().map(CategorySpiUtil::resolveSuitableSeason)
                    .collect(Collectors.toSet());
        } else {
            suitableSeasons = Collections.emptySet();
        }

        return new CategoryInfo(
                id, name, emptyIfNull(thumbnailUrl),
                categoryCode, cat, recommendCode,
                suitableSeasons
        );
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

    private static Season resolveSuitableSeason(SuitableSeason suitableSeason) {
        return switch (suitableSeason) {
            case SPRING -> Season.SPRING;
            case SUMMER -> Season.SUMMER;
            case AUTUMN -> Season.AUTUMN;
            case WINTER -> Season.WINTER;
            default -> Season.ANY;
        };
    }

    private static String emptyIfNull(String s) {
        return s == null ? "" : s;
    }

    static Pageable randomPageRequest(
            int pageSize, long totalElements
    ) {
        int total = totalElements > Integer.MAX_VALUE ?
                Integer.MAX_VALUE : (int) totalElements;

        int totalPages = Math.ceilDiv(total, pageSize);
        int randomPage = RAND.nextInt(totalPages);

        return PageRequest.of(randomPage, pageSize);
    }
}
