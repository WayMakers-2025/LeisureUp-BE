package org.leisureup.member.internal.domain;

import jakarta.persistence.*;
import lombok.*;
import org.leisureup.member.internal.domain.InterestInfo.*;
import org.leisureup.member.internal.domain.InterestInfo.With;
import org.leisureup.member.internal.dto.request.*;
import org.leisureup.member.internal.dto.request.SaveInterestRequest.*;

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

    public static InterestInfo of(SaveInterestRequest request) {
        int age = request.ageRange();
        var reqWith = request.with();
        var reqExp = request.experience();
        var reqType = request.type();
        var reqLeisureType = request.leisureType();
        var reqSeason = request.preferredSeason();
        var reqDifficulty = request.difficulty();

        return new InterestInfo(
                age,
                Util.with(reqWith), Util.experience(reqExp),
                Util.type(reqType), Util.leisureType(reqLeisureType),
                Util.preferredSeason(reqSeason), Util.difficulty(reqDifficulty)
        );
    }

    public static InterestInfo of(InterestInfo given) {
        InterestInfo info = new InterestInfo();
        info.with = given.with;
        info.experience = given.experience;
        info.leisureType = given.leisureType;
        info.preferredSeason = given.preferredSeason;
        info.difficulty = given.difficulty;
        info.type = given.type;
        return info;
    }

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

class Util {

    static With with(ReqWith reqWith) {
        return switch (reqWith) {
            case alone -> With.alone;
            case friend -> With.friend;
            case family -> With.family;
        };
    }

    static Experience experience(ReqExperience reqExp) {
        return switch (reqExp) {
            case first -> Experience.first;
            case couple -> Experience.couple;
            case several -> Experience.several;
        };
    }

    static Type type(ReqType reqType) {
        return switch (reqType) {
            case relieving -> Type.relieving;
            case thrilling -> Type.thrilling;
        };
    }

    static LeisureType leisureType(ReqLeisureType reqLeisureType) {
        return switch (reqLeisureType) {
            case water -> LeisureType.water;
            case earth -> LeisureType.earth;
            case sky -> LeisureType.sky;
            case any -> LeisureType.any;
        };
    }

    static PreferredSeason preferredSeason(ReqPreferredSeason reqSeason) {
        return switch (reqSeason) {
            case spring -> PreferredSeason.spring;
            case summer -> PreferredSeason.summer;
            case autumn -> PreferredSeason.autumn;
            case winter -> PreferredSeason.winter;
            case any -> PreferredSeason.any;
        };
    }

    static Difficulty difficulty(ReqDifficulty reqDifficulty) {
        return switch (reqDifficulty) {
            case low -> Difficulty.low;
            case medium -> Difficulty.medium;
            case high -> Difficulty.high;
        };
    }
}