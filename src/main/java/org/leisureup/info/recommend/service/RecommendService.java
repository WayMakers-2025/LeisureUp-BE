package org.leisureup.info.recommend.service;

import java.time.*;
import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.info.recommend.dto.*;
import org.leisureup.info.recommend.dto.response.*;
import org.leisureup.info.recommend.dto.response.LocationRecommendation.*;
import org.leisureup.location.spi.*;
import org.leisureup.member.spi.*;
import org.springframework.stereotype.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {

    /**
     * 사용자가 관심있는 카테고리를 추려낼 최대 개수
     */
    private static final int DEFAULT_RECOMMENDING_CATEGORY_SIZE = 10;

    /**
     * 장소 추천으로 제공할 최대 장소 개수
     */
    private static final int DEFAULT_RECOMMENDING_SIZE = 50;

    private final MemberSpi memberSpi;
    private final CategorySpi categorySpi;
    private final LocationQueryPort locationQueryPort;
    private final PrioritizingService prioritizingService;

    /**
     * 로그인 하지 않은 사용자에게 임의의 카테고리 (레저 종류) 목록을 추천
     */
    public List<CategoryRecommendation> recommendOnAnonymous() {

        List<CategoryInfo> categories
                = categorySpi.getAnyCategories(DEFAULT_RECOMMENDING_CATEGORY_SIZE);

        return categories.stream()
                .map(RecommendServiceUtil::toResponse)
                .toList();
    }


    /**
     * 로그인한 사용자에게 카테고리 (레저 종류) 목록을 추천
     */
    public List<CategoryRecommendation> recommendOnMember(
            Long memberId
    ) {

        Optional<InterestCode> optional = memberSpi.getInterest(memberId);

        // 질문 응답을 한지 않았으면 그냥 평이한 카테고리 추천
        if (optional.isEmpty()) {
            return this.recommendOnAnonymous();
        }

        // 모든 카테고리 중
        List<CategoryInfo> allCategories = categorySpi.getAllCategories();

        // 사용자가 관심있는 카테고리만 끌어내 반환한다.
        return prioritizingService.prioritize(
                        optional.get(), allCategories,
                        DEFAULT_RECOMMENDING_CATEGORY_SIZE
                ).stream()
                .map(RecommendServiceUtil::toResponse)
                .toList();
    }

    /**
     * 계절에 맞는 여행 장소 목록을 추천
     */
    public List<LocationRecommendation> recommendOnSeason() {

        // 모든 카테고리를 가져온다.
        // 카테고리 중 현재 계절에 적합한 카테고리만 filter, ID 만 가져온다.
        List<Long> seasonCategoryIds = categorySpi.getAllCategories().stream()
                .filter(RecommendServiceUtil::filterOnCurrentSeason)
                .map(CategoryInfo::id)
                .toList();

        // 카테고리에 속한 임의의 장소를 반환한다.
        return locationQueryPort.getAnyLocationsOnCategory(
                        DEFAULT_RECOMMENDING_SIZE, seasonCategoryIds
                ).stream()
                .map(RecommendServiceUtil::toResponse)
                .toList();
    }
}


class RecommendServiceUtil {

    static LocationRecommendation toResponse(LocationResponse location) {
        Long id = location.locationId();
        String name = location.title();
        var desc = location.description();
        String th1 = null, th2 = null;
        Category category = resolveCategory(location.category());

        if (desc != null) {
            th1 = desc.largeThumbnail();
            th2 = desc.smallThumbnail();
        }

        return new LocationRecommendation(
                id, name, emptyIfNull(th1), emptyIfNull(th2), category
        );
    }

    private static Category resolveCategory(Cat cat) {
        return switch (cat) {
            case EARTH -> Category.EARTH;
            case WATER -> Category.WATER;
            case SKY -> Category.SKY;
            default -> Category.OTHER;
        };
    }

    static boolean filterOnCurrentSeason(CategoryInfo categoryInfo) {

        if (categoryInfo == null) {
            return false;
        }

        Set<Season> seasons = categoryInfo.suitableSeasons();

        if (seasons == null || seasons.isEmpty()) {
            return false;
        }

        return seasons.contains(Season.ANY) || seasons.contains(getCurrentSeason());
    }

    static CategoryRecommendation toResponse(CategoryInfo categoryInfo) {
        return new CategoryRecommendation(
                categoryInfo.id(), categoryInfo.name(),
                categoryInfo.thumbnailUrl()
        );
    }

    static CategoryRecommendation toResponse(PrioritizedCategoryInfo categoryInfo) {
        return new CategoryRecommendation(
                categoryInfo.categoryId(),
                categoryInfo.name(),
                categoryInfo.thumbnailUrl()
        );
    }

    private static String emptyIfNull(String s) {
        return s == null ? "" : s;
    }

    private static Season getCurrentSeason() {
        int month = LocalDate.now().getMonth().getValue();

        if (3 <= month && month <= 5) {
            return Season.SPRING;
        }

        if (month <= 8) {
            return Season.SUMMER;
        }

        if (month <= 10) {
            return Season.AUTUMN;
        }

        return Season.WINTER;
    }
}