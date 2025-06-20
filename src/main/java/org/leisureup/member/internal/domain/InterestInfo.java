package org.leisureup.member.internal.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.leisureup.location.internal.domain.*;
import org.leisureup.member.internal.domain.InterestInfo.*;
import org.leisureup.member.internal.dto.request.*;

/**
 * 사용자 니즈 수집 응답 세부사항
 */
@Getter
@Setter(AccessLevel.PACKAGE)
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class InterestInfo {

    @Enumerated(EnumType.STRING)
    private AgeRange ageRange;

    /**
     * {@code representation} 는 다음 형태의 질문 응답을 아래와 같이 저장함.
     *
     * <pre>
     *     {@code
     *     1. 평소 레저 활동을 즐기는 편인가요? : (사용자 응답 a)
     *     - a. 아직 해본 적 없어요
     *     - b. 몇번 체험해봤어요
     *     - c 자주 즐기는 편이에요
     *
     *     2. 어떤 분위기의 활동에 더 끌리시나요? : (사용자 응답 b)
     *     - a. 잔잔하고 힐링되는 게 좋아요
     *     - b. 신나고 스릴있는 걸 원해요
     *
     *     3. (생략, 사용자 응답 a)
     *     }
     *     {@code a-b-a}
     * </pre>
     * <p>
     *
     * <ul>
     *      사용자 응답은
     *      <ul>
     *          <li>각 문항의 선택지 중 하나만 선택될 수 있음.</li>
     *          <li>{@code -} 를 통해 각 문항 응답이 구별됨.</li>
     *      </ul>
     *      즉, {@code a-b-cd}, {@code a,b,c} 와 같은 형태는 올바르지 않음.
     * </ul>
     *
     * @see CategoryRecommend
     */
    @NotBlank
    @Column(nullable = false)
    private String representation;

    public static InterestInfo of(SaveInterestRequest request) {
        return Util.toEmbedded(request);
    }

    public static InterestInfo of(InterestInfo given) {
        return Util.deepCopy(given);
    }

    public enum AgeRange {
        A_10, A_20, A_30, A_40_ABOVE
    }
}

class Util {

    static InterestInfo toEmbedded(SaveInterestRequest record) {
        InterestInfo info = new InterestInfo();

        AgeRange ageRange = switch (record.ageRange()) {
            case A_10 -> AgeRange.A_10;
            case A_20 -> AgeRange.A_20;
            case A_30 -> AgeRange.A_30;
            case A_40_ABOVE -> AgeRange.A_40_ABOVE;
        };

        info.setAgeRange(ageRange);
        info.setRepresentation(record.compositeCode());
        return info;
    }

    static InterestInfo deepCopy(InterestInfo given) {
        InterestInfo info = new InterestInfo();
        info.setAgeRange(given.getAgeRange());
        info.setRepresentation(given.getRepresentation());
        return info;
    }
}