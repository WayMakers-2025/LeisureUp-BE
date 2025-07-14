package org.leisureup.info.recommend.service;

import java.time.*;
import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.info.recommend.dto.*;
import org.leisureup.info.recommend.dto.response.*;
import org.leisureup.info.recommend.dto.response.LocationInfo.*;
import org.leisureup.location.spi.*;
import org.leisureup.member.spi.*;
import org.springframework.stereotype.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendService {

    private static final int DEFAULT_RECOMMENDING_SIZE = 50;
    private static final int DEFAULT_RECOMMENDING_CATEGORY_SIZE = 5;
    private final MemberSpi memberSpi;
    private final CategorySpi categorySpi;
    private final LocationQueryPort locationQueryPort;
    private final PrioritizingService prioritizingService;

    /**
     * 로그인 하지 않은 사용자에게 장소를 추천
     */
    public List<LocationInfo> recommendOnAnonymous() {

        List<LocationResponse> locations
                = locationQueryPort.getAnyLocations(DEFAULT_RECOMMENDING_SIZE);

        return locations.stream()
                .map(RecommendServiceUtil::toResponse)
                .toList();
    }


    /**
     * 로그인한 사용자에게 장소를 추천
     */
    public List<LocationInfo> recommendOnMember(
            Long memberId
    ) {

        Optional<InterestCode> optional = memberSpi.getInterest(memberId);

        // 질문 응답을 한지 않았으면 그냥 평이한 장소 추천
        if (optional.isEmpty()) {
            return this.recommendOnAnonymous();
        }

        // 모든 카테고리 중
        List<CategoryInfo> allCategories = categorySpi.getAllCategories();

        // 사용자가 관심있는 카테고리 ID 만 끌어낸다.
        List<Long> categoriesInInterest = prioritizingService.prioritize(
                        optional.get(), allCategories,
                        DEFAULT_RECOMMENDING_CATEGORY_SIZE
                ).stream().map(PrioritizedCategoryInfo::categoryId)
                .toList();

        // 정보 조회해 반환한다.
        return locationQueryPort.getAnyLocationsOnCategory(
                        DEFAULT_RECOMMENDING_SIZE, categoriesInInterest
                ).stream().map(RecommendServiceUtil::toResponse)
                .toList();
    }

    /**
     * 계절에 따른 레저 (카테고리) 목록을 추천
     */
    public List<RecommendOnSeason> recommendOnSeason() {

        // 모든 카테고리를 가져온다.
        List<CategoryInfo> allCategories = categorySpi.getAllCategories();

        // 카테고리 중 임의의 계절에 어올리거나 현재 계절에 맞는 카테고리만 filter 한다.
        return allCategories.stream()
                .filter(RecommendServiceUtil::filterOnCurrentSeason)
                .map(RecommendServiceUtil::toResponse)
                .toList();
    }
}


class RecommendServiceUtil {

    static LocationInfo toResponse(LocationResponse location) {
        Long id = location.locationId();
        String name = location.title();
        var desc = location.description();
        String th1 = null, th2 = null;
        Category category = resolveCategory(location.category());

        if (desc != null) {
            th1 = desc.largeThumbnail();
            th2 = desc.smallThumbnail();
        }

        return new LocationInfo(
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

    static RecommendOnSeason toResponse(CategoryInfo categoryInfo) {
        return new RecommendOnSeason(
                categoryInfo.id(), categoryInfo.name(),
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