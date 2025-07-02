package org.leisureup.info.recommend.dto;

import org.springframework.lang.*;

/**
 * @param score 높을수록 좋은거
 */
public record PrioritizedCategoryInfo(
        Long categoryId,
        String name,
        double score
) implements Comparable<PrioritizedCategoryInfo> {

    @Override
    public int compareTo(@NonNull PrioritizedCategoryInfo o) {
        // 이래야 높은 점수가 head 로 정렬
        return Double.compare(o.score, score);
    }
}
