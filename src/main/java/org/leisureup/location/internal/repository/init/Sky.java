package org.leisureup.location.internal.repository.init;

import static org.leisureup.location.internal.repository.init.Utils.*;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum Sky {

    Skydiving(
            "스카이다이빙", "A03040100",
            encodeCodes("bc", "b", "c", "c", "a")
    ), UltralightAircraft(
            "초경량비행", "A03040200",
            encodeCodes("bc", "b", "c", "c", "a")
    ), HangGlidingParagliding(
            "행글라이딩/패러글라이딩", "A03040300",
            encodeCodes("bc", "b", "c", "c", "a")
    ), HotAirBalloon(
            "열기구", "A03040400",
            encodeCodes("a", "a", "a", "c", "cd")
    );

    final String name, code, recommendCode;

}
