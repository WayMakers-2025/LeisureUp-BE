package org.leisureup.member.internal.dto.request;

import jakarta.validation.constraints.*;

public record SaveInterestRequest(
        @Positive int ageRange,

        @NotNull ReqWith with,
        @NotNull ReqExperience experience,
        @NotNull ReqType type,
        @NotNull ReqLeisureType leisureType,
        @NotNull ReqPreferredSeason preferredSeason,
        @NotNull ReqDifficulty difficulty
) {

    public enum ReqWith {
        alone, friend, family
    }

    public enum ReqExperience {
        first, couple, several
    }

    public enum ReqType {
        relieving, thrilling
    }

    public enum ReqLeisureType {
        water, earth, sky, any
    }

    public enum ReqPreferredSeason {
        spring, summer, autumn, winter, any
    }

    public enum ReqDifficulty {
        low, medium, high
    }
}
