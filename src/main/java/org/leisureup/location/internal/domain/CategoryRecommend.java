package org.leisureup.location.internal.domain;

import jakarta.persistence.*;
import lombok.*;
import org.leisureup.member.internal.domain.*;

/**
 * 사용자 질문 기반 카테고리 추천을 위한 embedded
 */
@Getter
@Setter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryRecommend {

    /**
     * {@code recommendingCode} 는 다음 형태의 질문 분류에 아래와 같은 형식으로 저장됨.
     *
     * <pre>
     *     {@code
     *     1. 평소 레저 활동을 즐기는 편인가요?
     *     - a. 아직 해본 적 없어요 : 인라인
     *     - b. 몇번 체험해봤어요
     *     - c 자주 즐기는 편이에요
     *
     *     2. 어떤 분위기의 활동에 더 끌리시나요?
     *     - a. 잔잔하고 힐링되는 게 좋아요 : 인라인
     *     - b. 신나고 스릴있는 걸 원해요 : 인라인
     *     }
     *     {@code a-ab}
     * </pre>
     *
     * <ul>
     *     {@code recommendingCode} 는
     *     <ul>
     *         <li>{@code 2} 문항처럼 한 문항의 여러 응답에 속할 수 있음.</li>
     *         <li>{@code -} 를 통해 각 문항별 속한 정보가 구별됨.</li>
     *     </ul>
     * </ul>
     *
     * @see InterestInfo
     */
    @Column(length = 100)
    private String recommendingCode;

    public static CategoryRecommend of(String code) {
        return new CategoryRecommend(code);
    }

    public static CategoryRecommend of(CategoryRecommend given) {
        return new CategoryRecommend(given.getRecommendingCode());
    }
}
