package org.leisureup.location.internal.repository.init;

import static org.leisureup.location.internal.repository.init.Utils.*;

import lombok.*;

@Getter
@RequiredArgsConstructor
public enum Earth {

    Inline(
            "인라인", "A03020400",
            encodeCodes("ab", "a", "ab", "b", "a")
    ), Bike(
            "자전거 하이킹", "A03020500",
            encodeCodes("ab", "a", "ab", "b", "ad")
    ), Cart(
            "카트", "A03020600",
            encodeCodes("a", "b", "a", "b", "bd")
    ), Golf(
            "골프", "A03020700",
            encodeCodes("ab", "a", "a", "bd", "ad")
    ), HorseRiding(
            "승마", "A03021100",
            encodeCodes("a", "a", "a", "b", "acd")
    ), SkiSnowboard(
            "스키/스노보드", "A03021200",
            encodeCodes("bc", "ab", "bc", "b", "bcd")
    ), Skate(
            "스케이트", "A03021300",
            encodeCodes("a", "a", "a", "bd", "bd")
    ), Sledding(
            "썰매장", "A03021400",
            encodeCodes("a", "a", "a", "bd", "bd")
    ), HuntingGround(
            "수렵장", "A03021500",
            encodeCodes("bc", "b", "bc", "b", "b")
    ), AutoCamping(
            "오토캠핑", "A03021700",
            ""
    ), ShootingRange(
            "사격장", "A03021600",
            encodeCodes("bc", "b", "bc", "bd", "b")
    ), RockClimbing(
            "암벽등반", "A03021800",
            encodeCodes("bc", "b", "bc", "bd", "a")
    ), SurvivalGaming(
            "서바이벌게임", "A03022000",
            encodeCodes("b", "b", "bc", "b", "b")
    ), ATV(
            "ATV", "A03022100",
            encodeCodes("bc", "b", "bc", "b", "b")
    ), MTB(
            "MTB", "A03022200",
            encodeCodes("bc", "b", "bc", "b", "b")
    ), OffRoad(
            "오프로드", "A03022300",
            encodeCodes("bc", "b", "bc", "b", "b")
    ), BungeeJumping(
            "번지점프", "A03022400",
            encodeCodes("bc", "b", "c", "b", "a")
    ), Tracking(
            "트래킹", "A03022700",
            encodeCodes("a", "a", "a", "b", "acd")
    );

    final String name, code, recommendCode;
}
