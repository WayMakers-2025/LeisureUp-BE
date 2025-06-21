package org.leisureup.info.recommend.internal.service;

import java.util.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.leisureup.info.recommend.internal.dto.*;
import org.leisureup.location.spi.*;
import org.leisureup.member.spi.*;
import org.springframework.stereotype.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrioritizingService {

    private final Scoring scoring;

    /**
     * 사용자 관심 코드에 맞춰 카테고리 중 상위 {@code truncate} 개만 추려 반환한다.
     */
    public List<PrioritizedCategoryInfo> prioritize(
            InterestCode memberCode, List<CategoryInfo> categories,
            int truncate
    ) {

        if (truncate <= 0) {
            throw new IllegalArgumentException("Truncate must be greater than zero");
        }

        if (categories.size() < truncate) {
            log.warn("Given categories are less than truncate");
        }

        return categories.stream()
                .map(c -> convert(memberCode, c))
                .sorted()
                .limit(truncate)
                .toList();
    }

    private PrioritizedCategoryInfo convert(
            InterestCode memberCode, CategoryInfo category
    ) {
        Long id = category.id();
        String name = category.name();

        String code1 = memberCode.code();
        String code2 = category.recommendingCode();

        return new PrioritizedCategoryInfo(
                id, name, scoring.score(code1, code2)
        );
    }
}
