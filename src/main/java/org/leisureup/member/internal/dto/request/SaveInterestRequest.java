package org.leisureup.member.internal.dto.request;

import lombok.*;
import org.leisureup.global.validation.*;

public record SaveInterestRequest(
        @ValidEnum(
                target = AgeRange.class,
                message = "ageRange 을 매칭할 수 없습니다."
        )
        AgeRange ageRange,

        @ValidEnum(
                target = LeisureUsual.class,
                message = "leisureUsual 을 매칭할 수 없습니다."
        )
        LeisureUsual leisureUsual,

        @ValidEnum(
                target = PreferredStyle.class,
                message = "preferredStyle 을 매칭할 수 없습니다."
        )
        PreferredStyle preferredStyle,

        @ValidEnum(
                target = Intensity.class,
                message = "intensity 을 매칭할 수 없습니다."
        )
        Intensity intensity,

        @ValidEnum(
                target = Theme.class,
                message = "theme 을 매칭할 수 없습니다."
        )
        Theme theme,

        @ValidEnum(
                target = AlongWith.class,
                message = "alongWith 을 매칭할 수 없습니다."
        )
        AlongWith alongWith
) implements CodePresentable {

    @Override
    public String compositeCode() {
        return String.join(
                "-", leisureUsual.getCode(), preferredStyle.getCode(),
                intensity.getCode(), theme.getCode(), alongWith.getCode()
        );
    }

    public enum AgeRange {
        A_10            // 10 대
        , A_20          // 20 대
        , A_30          // 30 대
        , A_40_ABOVE    // 40 대 이상
    }

    @Getter
    @RequiredArgsConstructor
    public enum LeisureUsual {
        never("a")       // 아직 해본 적 없어요
        , couple("b")    // 몇번 체험해 봤어요
        , many("c")      // 자주 즐기는 편이에요
        ;
        final String code;
    }

    @Getter
    @RequiredArgsConstructor
    public enum PreferredStyle {
        relieving("a")       // 잔잔하고 힐되는 게 좋아요
        , thrilling("b")     // 신나고 스있는 걸 원해요
        ;
        final String code;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Intensity {
        light("a")           // 가볍게 즐길 수 있는 체험
        , active("b")        // 적당히 움이는 활동
        , challenging("c")   // 도전적인 활동
        ;
        final String code;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Theme {
        water("a")       // 물에서 하는 레저
        , earth("b")     // 땅 위에서 하는 레저
        , sky("c")       // 하늘에서 하는 레저
        , inDoor("d")    // 날씨와 상관없는 실내 활동
        ;
        final String code;
    }

    @Getter
    @RequiredArgsConstructor
    public enum AlongWith {
        alone("a")       // 혼자
        , friend("b")    // 친구
        , couple("c")    // 연인
        , family("d")    // 가족
        ;
        final String code;
    }
}
