package org.leisureup.location.internal.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자 질문 기반 카테고리 추천을 위한 embedded
 */
@Getter
@Setter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryRecommend {

    @Column(length = 100)
    private String recommendingCode;

    public static CategoryRecommend of(String code) {
        return new CategoryRecommend(code);
    }

    public static CategoryRecommend of(CategoryRecommend given) {
        return new CategoryRecommend(given.getRecommendingCode());
    }
}
