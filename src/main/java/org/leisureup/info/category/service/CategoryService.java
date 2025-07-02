package org.leisureup.info.category.service;

import java.util.*;
import java.util.function.*;
import lombok.*;
import org.leisureup.info.category.dto.response.*;
import org.leisureup.location.spi.*;
import org.springframework.stereotype.*;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategorySpi categorySpi;

    /**
     * 땅, 물, 하늘 종류에 속한 카테고리들의 간략한 정보를 조회한다.
     */
    public GetAllCategoriesResponse getAllCategories() {

        List<CategoryInfo> allCategories = categorySpi.getAllCategories();

        var earths = CategoryServiceUtil.toResponse(
                allCategories, CategoryServiceUtil::filterEarths
        );
        var waters = CategoryServiceUtil.toResponse(
                allCategories, CategoryServiceUtil::filterWaters
        );
        var skies = CategoryServiceUtil.toResponse(
                allCategories, CategoryServiceUtil::filterSkies
        );

        return GetAllCategoriesResponse.of(earths, waters, skies);
    }
}

class CategoryServiceUtil {

    private static boolean filter(
            CategoryInfo cat,
            Cat categoryType
    ) {
        return cat.category().equals(categoryType);
    }

    static boolean filterEarths(CategoryInfo cat) {
        return filter(cat, Cat.EARTH);
    }

    static boolean filterWaters(CategoryInfo cat) {
        return filter(cat, Cat.WATER);
    }

    static boolean filterSkies(CategoryInfo cat) {
        return filter(cat, Cat.SKY);
    }

    private static CategoryInfoResponse toResponse(CategoryInfo cat) {
        return new CategoryInfoResponse(cat.id(), cat.name());
    }

    static List<CategoryInfoResponse> toResponse(
            List<CategoryInfo> cats, Predicate<CategoryInfo> filter
    ) {
        return cats.stream()
                .filter(filter)
                .map(CategoryServiceUtil::toResponse)
                .toList();
    }
}
