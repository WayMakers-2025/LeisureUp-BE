package org.leisureup.info.recommend.service;

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
                .map(RecommendServiceUtil::convert)
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
                ).stream().map(RecommendServiceUtil::convert)
                .toList();
    }
}


class RecommendServiceUtil {

    static LocationInfo convert(LocationResponse location) {
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

    private static String emptyIfNull(String s) {
        return s == null ? "" : s;
    }
}