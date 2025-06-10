package org.leisureup.member.internal.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자 니즈 수집 응답 세부사항
 */
@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class InterestInfo {

    private int ageRange;

    @Enumerated(EnumType.STRING)
    private With with;

    @Enumerated(EnumType.STRING)
    private Experience experience;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private LeisureType leisureType;

    @Enumerated(EnumType.STRING)
    private PreferredSeason preferredSeason;

    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;

    public enum With {
        alone, friend, family
    }

    public enum Experience {
        first, couple, several
    }

    public enum Type {
        relieving, thrilling
    }

    public enum LeisureType {
        water, earth, sky, any
    }

    public enum PreferredSeason {
        spring, summer, autumn, winter, any
    }

    public enum Difficulty {
        low, medium, high
    }
}