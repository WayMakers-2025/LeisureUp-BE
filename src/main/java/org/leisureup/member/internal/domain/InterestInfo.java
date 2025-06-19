package org.leisureup.member.internal.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
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